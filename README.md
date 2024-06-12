# Requirement :
- Since app is using **fibrebase ML** kit to translate language , so it Requirs a **.json file** to connect our application to firebase.
# Procedure:
1. Go to https://console.firebase.google.com/
2. click on add project.
3. Specify the project name.
4. Under configure google analytics select account as **Default account for firebase**.
5. After project creation click on continue.
6. Then register app as android.
7. Then Specify the package name.
8. Download .json file and paste it into src folder located in LanguageTranslatorApp/app/src .
9. **NOTE** Downloaded json file should be renamed as **google-service.json** .
10. Then go to **Tool** section in Android Studio , Select **Firebase**.
11. Under Firebase ML select **Use Firebase ML to recognize text in image** .
12. Then under 3 select **Add Firebase ML to your app** .
