def call(String url, String buildNumber){
    tx = url.toURL().openConnection().with {
        setRequestProperty("User-Agent", "Firefox/2.0.0.4")
        inputStream.with {
        	def ret = getText( 'UTF-8' )
        	close()
        	return ret.contains("Build number: ${buildNumber}")
        }
    }
}