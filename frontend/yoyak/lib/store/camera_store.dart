import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'package:path/path.dart' as path;

import '../screen/Challenge/upload_challenge_screen.dart';

class CameraStore extends ChangeNotifier {
  XFile? image; // 이미지 담을 변수
  final ImagePicker picker = ImagePicker(); // Image Picker 초기화

  Future sendImageToServer() async {
    print("이미지 경로 : ${image!.path}");
    String fileName = path.basename(image!.path);
    print("이미지 네임 : ${fileName}");

    FormData formData = FormData.fromMap({
      'image': await MultipartFile.fromFile(image!.path, filename: fileName),
    });

    try {
      Dio dio = Dio();
      Response response = await dio.post(
        'https://j10b102.p.ssafy.io/api/recognition/upload',
        data: formData,
        options: Options(
          contentType: 'multipart/form-data',
        ),
      );

      if (response.statusCode == 200) {
        print(response.data);
      } else {
        print(response.statusCode);
      }
    } catch (e) {
      print('Error: $e');
    }
  }

  Future getImage(ImageSource imageSource) async {
    // pickedFile에 ImagePicker로 가져온 이미지가 담김
    final pickedFile = await picker.pickImage(
      source: imageSource,
      imageQuality: 10,
    );
    if (pickedFile != null) {
      image = XFile(pickedFile.path); // 가져온 이미지를 image에 저장
      notifyListeners();
    } else {
      print("고른 이미지 null 값임");
    }
  }

  Future getImageAndNavigate(ImageSource imageSource, BuildContext context) async {
    // pickedFile에 ImagePicker로 가져온 이미지가 담김
    final pickedFile = await picker.pickImage(
      source: imageSource,
      imageQuality: 10,
    );
    if (pickedFile != null) {
      image = await XFile(pickedFile.path); // 가져온 이미지를 image에 저장

      Navigator.pop(context);
      Navigator.push(context,
          MaterialPageRoute(builder: (context) => const UploadChallengeScreen()));

      notifyListeners();
    } else {
      print("고른 이미지 null 값임");
    }
  }
}
