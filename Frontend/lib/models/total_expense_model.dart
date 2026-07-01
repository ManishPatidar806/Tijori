class CategoryTotals {
  final int id;
  final double groceries;
  final double medical;
  final double domestic;
  final double shopping;
  final double bills;
  final double entertainment;
  final double travelling;
  final double fueling;
  final double educational;
  final double others;
  final DateTime datetime;

  CategoryTotals({
    required this.id,
    required this.groceries,
    required this.medical,
    required this.domestic,
    required this.shopping,
    required this.bills,
    required this.entertainment,
    required this.travelling,
    required this.fueling,
    required this.educational,
    required this.others,
    required this.datetime,
  });

  factory CategoryTotals.fromJson(Map<String, dynamic> json) {
    return CategoryTotals(
      id: json['id'] ?? 0,
      groceries: (json['groceries'] ?? 0).toDouble(),
      medical: (json['medical'] ?? 0).toDouble(),
      domestic: (json['domestic'] ?? 0).toDouble(),
      shopping: (json['shopping'] ?? 0).toDouble(),
      bills: (json['bills'] ?? 0).toDouble(),
      entertainment: (json['entertainment'] ?? 0).toDouble(),
      travelling: (json['travelling'] ?? 0).toDouble(),
      fueling: (json['fueling'] ?? 0).toDouble(),
      educational: (json['educational'] ?? 0).toDouble(),
      others: (json['others'] ?? 0).toDouble(),
      datetime: _parseDateTime(json['datetime']),
    );
  }

  factory CategoryTotals.fromCategoryBudgets(List<dynamic> budgets) {
    final totals = <String, double>{
      'GROCERIES': 0,
      'MEDICAL': 0,
      'DOMESTIC': 0,
      'SHOPPING': 0,
      'BILLS': 0,
      'ENTERTAINMENT': 0,
      'TRAVELLING': 0,
      'FUELING': 0,
      'EDUCATIONAL': 0,
      'OTHERS': 0,
    };

    for (final item in budgets) {
      if (item is Map<String, dynamic>) {
        final category = (item['categoryType'] ?? item['category'])?.toString().toUpperCase();
        final amount = (item['totalAmount'] ?? item['amount'] ?? 0).toDouble();
        if (category != null && totals.containsKey(category)) {
          totals[category] = amount;
        }
      }
    }

    return CategoryTotals(
      id: 0,
      groceries: totals['GROCERIES'] ?? 0,
      medical: totals['MEDICAL'] ?? 0,
      domestic: totals['DOMESTIC'] ?? 0,
      shopping: totals['SHOPPING'] ?? 0,
      bills: totals['BILLS'] ?? 0,
      entertainment: totals['ENTERTAINMENT'] ?? 0,
      travelling: totals['TRAVELLING'] ?? 0,
      fueling: totals['FUELING'] ?? 0,
      educational: totals['EDUCATIONAL'] ?? 0,
      others: totals['OTHERS'] ?? 0,
      datetime: DateTime.now(),
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'groceries': groceries,
      'medical': medical,
      'domestic': domestic,
      'shopping': shopping,
      'bills': bills,
      'entertainment': entertainment,
      'travelling': travelling,
      'fueling': fueling,
      'educational': educational,
      'others': others,
      'datetime': datetime.toIso8601String(),
    };
  }

  static DateTime _parseDateTime(dynamic dt) {
    if (dt == null) return DateTime.now();
    if (dt is DateTime) return dt;
    if (dt is String) return DateTime.tryParse(dt) ?? DateTime.now();
    return DateTime.now(); // fallback
  }
}
