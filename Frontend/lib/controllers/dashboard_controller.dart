import 'dart:convert';
import 'package:new_minor/api/auth_api_client.dart';
import '../models/dashboard_data_model.dart';
import '../api/api_urls.dart';

class DashboardController {
  static Future<DashboardDataModel?> fetchDashboardData() async {
    final url = Uri.parse('${ApiUrls.baseURL}/api/users/profile');

    try {
      final response = await AuthApiClient.get(url);

      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
        if (data['data'] != null) {
          return DashboardDataModel.fromJson(data['data']);
        } else {
          print("Invalid dashboard data format.");
        }
      } else {
        print("Failed to load dashboard data: ${response.statusCode}");
      }
    } catch (e) {
      print("Not fetching dashboard Data: $e");
    }

    return null;
  }
}
