def call(String name, String tag) {
    docker.withServer('unix:///var/run/docker.sock') {
	    docker.withRegistry('', 'nexus-repo') {
	        	dockerImage = docker.build(name, "./")
	        dockerImage.push(tag)
	    }
	}
}