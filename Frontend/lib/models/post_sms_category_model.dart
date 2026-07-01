class CategorizedSmsData {
  final int id;
  final double amount;
  final String? refNo;
  String? category;
  final String moneyType;
  final DateTime dateTime;

  CategorizedSmsData({
    required this.id,
    required this.amount,
    required this.refNo,
    required this.category,
    required this.moneyType,
    required this.dateTime,
  });

  factory CategorizedSmsData.fromJson(Map<String, dynamic> json) {
    return CategorizedSmsData(
      id: json['id'],
      amount: (json['amount'] as num).toDouble(),
      refNo: json['refNo'] ?? json['referenceNumber'],
      category: json['category'] ?? json['categoryType'],
      moneyType: json['moneyType'] ?? json['transactionType'],
      dateTime: DateTime.parse(json['dateTime']),
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'amount': amount,
      'referenceNumber': refNo,
      'category': category?.toUpperCase(),
      'transactionType': moneyType.toUpperCase(),
      'dateTime': dateTime.toIso8601String(),
    };
  }

  Map<String, dynamic> toBackendJson() {
    return {
      'id': id,
      'amount': amount,
      'referenceNumber': refNo,
      'category': category?.toUpperCase(),
      'transactionType': moneyType.toUpperCase(),
      'dateTime': dateTime.toIso8601String(),
    };
  }
}
