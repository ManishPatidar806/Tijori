class ApiResponseHelper {
  static dynamic payload(Map<String, dynamic> responseBody) {
    return responseBody['data'] ?? responseBody;
  }

  static bool success(Map<String, dynamic> responseBody) {
    final dynamic value = responseBody['success'] ?? responseBody['status'];
    return value == true;
  }

  static String message(Map<String, dynamic> responseBody) {
    final dynamic value = responseBody['message'];
    return value is String ? value : '';
  }
}