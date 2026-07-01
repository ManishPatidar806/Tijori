import 'dart:convert';
import 'package:new_minor/api/auth_api_client.dart';
import '../api/api_urls.dart';
import '../models/post_sms_category_model.dart';

class CategoryTransactionController {
  static Future<List<CategorizedSmsData>> fetchTransactionsByCategory(String category) async {
    final url = Uri.parse('${ApiUrls.baseURL}/v1/api/transactions/category?category=${category.toUpperCase()}');

    try {
      final response = await AuthApiClient.get(url);

      if (response.statusCode == 200) {
        final body = jsonDecode(response.body) as Map<String, dynamic>;
        final dynamic payload = body['data'] ?? body;

        if (payload is List) {
          return payload
              .map((item) => CategorizedSmsData.fromJson(item as Map<String, dynamic>))
              .toList();
        } else {
          print("No list found in ApiResponse.data");
        }
      } else {
        print("Failed to fetch transactions: ${response.statusCode}");
      }
    } catch (e) {
      print("Error fetching transactions: $e");
    }

    return [];
  }
}
