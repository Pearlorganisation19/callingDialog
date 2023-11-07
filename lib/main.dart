import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  static const platform = MethodChannel('incoming_call_channel');

  @override
  void initState() {
    super.initState();
    startForegroundService();
  }

  void startForegroundService() async {
    try {
      await platform.invokeMethod('startForegroundService');
    } on PlatformException catch (e) {
      print("Error: $e");
    }
  }

  void stopForegroundService() async {
    try {
      await platform.invokeMethod('stopForegroundService');
    } on PlatformException catch (e) {
      print("Error: $e");
    }
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: Text('Call State Detection'),
        ),
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              Text('Listening for incoming calls...'),
              SizedBox(height: 20),
              ElevatedButton(
                onPressed: startForegroundService,
                child: Text('Start Foreground Service'),
              ),
              ElevatedButton(
                onPressed: stopForegroundService,
                child: Text('Stop Foreground Service'),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
