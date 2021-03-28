def call(String url, String buildNumber){
    def urlText = new URL(url).getText()
  	return urlText.contains("Build number: ${buildNumber}")
}