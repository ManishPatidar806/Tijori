import 'package:new_minor/api/auth_api_client.dart';
import 'package:new_minor/api/api_urls.dart';
import '../models/income.dart';


class IncomeController {
  Future<bool> submitIncome(Income incomeData) async {
    final url = Uri.parse(
      '${ApiUrls.baseURL}/v1/api/account-balance/income?saving=${incomeData.targetSaving}&income=${incomeData.monthlyIncome}',
    );
    final response = await AuthApiClient.post(url);

    if (response.statusCode == 200) {
      print("Income data submitted successfully.");
      return true;
    } else {
      print("Failed to submit income data: ${response.body}");
      return false;
    }
  }
}
