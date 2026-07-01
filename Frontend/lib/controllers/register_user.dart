import 'dart:convert';
import 'package:new_minor/api/api_response_helper.dart';
import 'package:new_minor/api/auth_api_client.dart';
import 'package:new_minor/api/secure_helper_functions.dart';
import '../api/api_urls.dart';
import '../models/user.dart';

class ApiService {
  static const String _baseUrl = ApiUrls.baseURL;

  static Future<bool> registerUser(User user) async {
    final url = Uri.parse('$_baseUrl/api/users/signup');
    final response = await AuthApiClient.post(
      url,
      authenticated: false,
      body: user.toJson(),
    );

    print('Response $response');
    print(response.body);

    if (response.statusCode == 200 || response.statusCode == 201) {
      final body = jsonDecode(response.body) as Map<String, dynamic>;
      final dynamic payload = ApiResponseHelper.payload(body);

      if (payload is Map<String, dynamic>) {
        final accessToken = payload['accessToken'] ?? payload['token'];
        final refreshToken = payload['refreshToken'];

        if (accessToken is String && refreshToken is String) {
          await SecureStorageHelper.saveSession(
            accessToken: accessToken,
            refreshToken: refreshToken,
          );
          print("Saved token pair successfully");
        }
      }

      return true;
    } else if (response.statusCode == 409) {
      throw Exception('User already exists');
    } else {
      throw Exception('Failed to register user');
    }
  }

  static Future<String?> fetchJwtToken() async {
    final token = await SecureStorageHelper.getAccessToken();
    if (token == null) {
      print("No JWT token found, user is not authenticated.");
    } else {
      print("Fetched token: $token");
    }
    return token;
  }
}
