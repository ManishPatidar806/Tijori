import 'dart:convert';
import 'package:new_minor/api/auth_api_client.dart';
import 'package:new_minor/models/post_sms_category_model.dart';
import '../api/api_urls.dart';


class SmsFetchController {
  static Future<List<CategorizedSmsData>> fetchCategorizedSms() async {
    final url = Uri.parse("${ApiUrls.baseURL}/v1/api/transactions/categorization");

    try {
      final response = await AuthApiClient.get(url);

      if (response.statusCode == 200) {
        final jsonResponse = jsonDecode(response.body) as Map<String, dynamic>;
        final dynamic payload = jsonResponse['data'] ?? jsonResponse;
        if (payload is List) {
          return payload
              .map((json) => CategorizedSmsData.fromJson(json as Map<String, dynamic>))
              .toList();
        } else {
          print("Invalid structure: expected a list in ApiResponse.data.");
        }
      } else {
        print("Error fetching data: ${response.statusCode}");
      }
    } catch (e) {
      print("Exception during fetch: $e");
    }

    return [];
  }
}
