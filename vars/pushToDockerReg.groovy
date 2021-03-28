def call(String name, String tag, String server) {
    docker.withServer(server) {
        docker.withTool('docker'){
	        docker.withRegistry('https://docker.rrohau.lab.playpit.by', 'nexus-repo') {
	            	dockerImage = docker.build(name, "./")
	            dockerImage.push(tag)
	        }
	    }
    }   
}