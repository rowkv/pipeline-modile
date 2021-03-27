def call(String name, String tag) {
    docker.withServer('tcp://35.188.219.193:2376') {
        docker.withTool('docker'){
	        docker.withRegistry('https://docker.rrohau.lab.playpit.by', 'nexus-repo') {
	            	dockerImage = docker.build(name, "./")
	            dockerImage.push(tag)
	        }
	    }
    }   
}