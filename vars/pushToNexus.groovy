def call(String filename, String type ,String artid, String gid, String repo, String version='${BUILD_NUMBER}') {
    nexusArtifactUploader(
        nexusVersion: 'nexus3',
        protocol: 'https',
        nexusUrl: 'nexus.rrohau.lab.playpit.by',
        groupId: gid,
        version: version,
        repository: repo,
        credentialsId: 'nexus-repo',
        artifacts: [
            [artifactId: artid,
            classifier: '',
            file: filename + '.' + type,
            type: type]
        ]
    )
}
