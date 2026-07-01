import 'package:new_minor/api/auth_api_client.dart';
import '../api/api_urls.dart';
import '../models/sms_data.dart';

class SmsService {
  static Future<void> sendSmsListToBackend(List<SmsData> smsList) async {
    final payload = SmsPayload(sms: smsList);
    final uri = Uri.parse('${ApiUrls.baseURL}/v1/api/transactions/save');

    try {
      final response = await AuthApiClient.post(
        uri,
        body: payload.toJson(),
      );

      print("Response: ${response.statusCode} - ${response.body}");

      if (response.statusCode == 200 || response.statusCode == 204) {
        print("SMS data list sent successfully");
      } else {
        print("Failed to send SMS data list: ${response.statusCode}");
      }
    } catch (e) {
      print("Error sending SMS list: $e");
    }
  }
}
