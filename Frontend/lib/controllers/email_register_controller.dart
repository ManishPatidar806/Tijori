import 'package:new_minor/api/api_response_helper.dart';
import 'package:new_minor/api/auth_api_client.dart';
import 'package:new_minor/api/api_urls.dart';

class EmailOtpService {
  static Future<bool> sendOtpToEmail(String email) async {
    try {
      final url = Uri.parse('${ApiUrls.baseURL}/v1/api/emailVerification/sendOtp?email=$email');

      final response = await AuthApiClient.get(url, authenticated: false);

      print('Send OTP - Status Code: ${response.statusCode}');
      print('Send OTP - Response Body: ${response.body}');

      if (response.statusCode == 200) {
        final body = jsonDecode(response.body) as Map<String, dynamic>;
        return ApiResponseHelper.success(body);
      } else {
        throw Exception("Failed to send OTP: ${response.statusCode}");
      }
    } catch (e) {
      print("Send OTP Error: $e");
      throw Exception("Failed to send OTP: $e");
    }
  }

  static Future<bool> verifyEmailOtp(String email, String otp) async {
    try {
      final url = Uri.parse('${ApiUrls.baseURL}/v1/api/emailVerification/verifyOtp?email=$email&otp=$otp');

      final response = await AuthApiClient.get(url, authenticated: false);

      print('Verify OTP - Status Code: ${response.statusCode}');
      print('Verify OTP - Response Body: ${response.body}');

      if (response.statusCode == 200) {
        final body = jsonDecode(response.body) as Map<String, dynamic>;
        return ApiResponseHelper.success(body);
      } else {
        throw Exception("OTP Verification Failed: ${response.statusCode}");
      }
    } catch (e) {
      print("Verify OTP Error: $e");
      throw Exception("OTP Verification Failed: $e");
    }
  }
}
