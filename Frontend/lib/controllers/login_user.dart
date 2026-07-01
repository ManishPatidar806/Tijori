import 'dart:convert';
import 'package:new_minor/api/api_response_helper.dart';
import 'package:new_minor/api/auth_api_client.dart';
import 'package:new_minor/api/secure_helper_functions.dart';
import '../api/api_urls.dart';
import '../models/login_model.dart';

class AuthService {
  Future<String> login(LoginRequest request) async {
    try {
      final response = await AuthApiClient.post(
        Uri.parse("${ApiUrls.baseURL}/api/users/signin"),
        authenticated: false,
        body: request.toJson(),
      );

      if (response.statusCode == 200 || response.statusCode == 201){
        final responseData = jsonDecode(response.body) as Map<String, dynamic>;
        final dynamic payload = ApiResponseHelper.payload(responseData);
        if (payload is Map<String, dynamic>) {
          final accessToken = payload['accessToken'] ?? payload['token'];
          final refreshToken = payload['refreshToken'];

          if (accessToken is String && refreshToken is String) {
            await SecureStorageHelper.saveSession(
              accessToken: accessToken,
              refreshToken: refreshToken,
            );
            print("Token pair saved successfully");
          }
        }
        return "Success";
      } else {
        return "Failed";
      }
    } catch (e) {
      return "Error: $e";
    }
  }
}
