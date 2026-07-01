import 'dart:convert';
import 'package:new_minor/api/auth_api_client.dart';
import 'package:new_minor/models/post_sms_category_model.dart';
import '../api/api_urls.dart';

class SmsPostController {
  static Future<bool> submitCategorizedSms(List<CategorizedSmsData> smsList) async {
    final url = Uri.parse("${ApiUrls.baseURL}/v1/api/transactions/categorization");

    try {
      final response = await AuthApiClient.post(
        url,
        body: {
          'transactions': smsList.map((sms) => sms.toBackendJson()).toList(),
        },
      );

      if (response.statusCode == 200) {
        final jsonResponse = jsonDecode(response.body) as Map<String, dynamic>;
        return jsonResponse['success'] == true;
      } else {
        print("Failed to post data: ${response.statusCode}");
      }
    } catch (e) {
      print("Exception during post: $e");
    }

    return false;
  }
}
