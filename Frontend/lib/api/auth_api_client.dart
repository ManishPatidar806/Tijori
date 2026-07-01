import 'dart:convert';

import 'package:http/http.dart' as http;

import 'api_urls.dart';
import 'secure_helper_functions.dart';

class AuthApiClient {
  static final http.Client _client = http.Client();

  static Future<http.Response> get(
    Uri uri, {
    Map<String, String>? headers,
    bool authenticated = true,
  }) async {
    return _sendWithRefresh(
      (requestHeaders) => _client.get(uri, headers: requestHeaders),
      headers: headers,
      authenticated: authenticated,
    );
  }

  static Future<http.Response> post(
    Uri uri, {
    Object? body,
    Map<String, String>? headers,
    bool authenticated = true,
  }) async {
    final preparedBody = body == null
        ? null
        : body is String
            ? body
            : jsonEncode(body);

    return _sendWithRefresh(
      (requestHeaders) => _client.post(uri, headers: requestHeaders, body: preparedBody),
      headers: headers,
      authenticated: authenticated,
    );
  }

  static Future<http.Response> _sendWithRefresh(
    Future<http.Response> Function(Map<String, String>) request, {
    Map<String, String>? headers,
    required bool authenticated,
  }) async {
    final initialHeaders = await _buildHeaders(headers: headers, authenticated: authenticated);
    final response = await request(initialHeaders);

    if (!authenticated || response.statusCode != 401) {
      return response;
    }

    final refreshed = await _refreshSession();
    if (!refreshed) {
      return response;
    }

    final retryHeaders = await _buildHeaders(headers: headers, authenticated: authenticated);
    return request(retryHeaders);
  }

  static Future<Map<String, String>> _buildHeaders({
    Map<String, String>? headers,
    required bool authenticated,
  }) async {
    final mergedHeaders = <String, String>{
      'Content-Type': 'application/json',
      if (headers != null) ...headers,
    };

    if (authenticated) {
      final accessToken = await SecureStorageHelper.getAccessToken();
      if (accessToken != null && accessToken.isNotEmpty) {
        mergedHeaders['Authorization'] = 'Bearer $accessToken';
      }
    }

    return mergedHeaders;
  }

  static Future<bool> _refreshSession() async {
    final refreshToken = await SecureStorageHelper.getRefreshToken();
    if (refreshToken == null || refreshToken.isEmpty) {
      return false;
    }

    try {
      final response = await http.post(
        Uri.parse('${ApiUrls.baseURL}/api/users/refresh'),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode({'refreshToken': refreshToken}),
      );

      if (response.statusCode != 200) {
        await SecureStorageHelper.deleteTokens();
        return false;
      }

      final responseBody = jsonDecode(response.body) as Map<String, dynamic>;
      final dynamic payload = responseBody['data'] ?? responseBody;
      if (payload is Map<String, dynamic>) {
        final accessToken = payload['accessToken'] ?? payload['token'];
        final nextRefreshToken = payload['refreshToken'];

        if (accessToken is String && nextRefreshToken is String) {
          await SecureStorageHelper.saveSession(
            accessToken: accessToken,
            refreshToken: nextRefreshToken,
          );
          return true;
        }
      }

      await SecureStorageHelper.deleteTokens();
      return false;
    } catch (_) {
      await SecureStorageHelper.deleteTokens();
      return false;
    }
  }

  static Future<bool> logout() async {
    final refreshToken = await SecureStorageHelper.getRefreshToken();
    if (refreshToken == null || refreshToken.isEmpty) {
      await SecureStorageHelper.deleteTokens();
      return true;
    }

    try {
      final response = await http.post(
        Uri.parse('${ApiUrls.baseURL}/api/users/logout'),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode({'refreshToken': refreshToken}),
      );

      await SecureStorageHelper.deleteTokens();
      return response.statusCode >= 200 && response.statusCode < 300;
    } catch (_) {
      await SecureStorageHelper.deleteTokens();
      return false;
    }
  }
}