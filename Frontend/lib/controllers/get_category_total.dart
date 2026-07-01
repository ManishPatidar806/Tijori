import 'dart:convert';
import 'package:new_minor/api/auth_api_client.dart';
import '../api/api_urls.dart';
import '../models/total_expense_model.dart';

class GetCategoryTotalsController {
  static Future<CategoryTotals?> fetchCategoryTotals() async {
    final url = Uri.parse("${ApiUrls.baseURL}/v1/api/category-budgets/amounts");

    try {
      final response = await AuthApiClient.get(url);

      if (response.statusCode == 200) {
        final Map<String, dynamic> data = jsonDecode(response.body) as Map<String, dynamic>;
        final dynamic payload = data['data'] ?? data;
        if (payload is List) {
          return CategoryTotals.fromCategoryBudgets(payload);
        }
        print("Invalid category totals payload: expected a list.");
      } else {
        print("Failed to fetch category totals: ${response.statusCode}");
      }
    } catch (e) {
      print("Error fetching category totals: $e");
    }

    return null;
  }
}
