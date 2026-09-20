import 'package:flutter/material.dart';

class AppTheme {
  static const Color primary = Color(0xFF009CA6);
  static const Color primaryDark = Color(0xFF006D75);
  static const Color secondary = Color(0xFF2C7BE5);
  static const Color background = Color(0xFFF4FBFB);
  static const Color card = Color(0xFFFFFFFF);
  static const Color text = Color(0xFF173B45);
  static const Color muted = Color(0xFF607D86);
  static const Color success = Color(0xFF2EAD68);
  static const Color warning = Color(0xFFFFB020);

  static ThemeData get lightTheme {
    final colorScheme = ColorScheme.fromSeed(
      seedColor: primary,
      primary: primary,
      secondary: secondary,
      surface: card,
      background: background,
    );

    return ThemeData(
      useMaterial3: true,
      colorScheme: colorScheme,
      scaffoldBackgroundColor: background,
      fontFamily: 'Roboto',
      appBarTheme: const AppBarTheme(
        centerTitle: false,
        backgroundColor: background,
        foregroundColor: text,
        elevation: 0,
      ),
      cardTheme: CardThemeData(
        color: card,
        elevation: 1.5,
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(22)),
        margin: EdgeInsets.zero,
      ),
      elevatedButtonTheme: ElevatedButtonThemeData(
        style: ElevatedButton.styleFrom(
          backgroundColor: primary,
          foregroundColor: Colors.white,
          padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 14),
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(18)),
          textStyle: const TextStyle(fontWeight: FontWeight.w700),
        ),
      ),
      inputDecorationTheme: InputDecorationTheme(
        filled: true,
        fillColor: Colors.white,
        border: OutlineInputBorder(
          borderRadius: BorderRadius.circular(16),
          borderSide: BorderSide.none,
        ),
      ),
    );
  }
}
