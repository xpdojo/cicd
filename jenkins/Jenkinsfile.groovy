// https://github.com/markruler/naver-works-plugin
node {
    stage('Hello') {
        naver(
            credentialId: 'naver-works-credential',
            botId: env.BOT_ID,
            channelId: env.CHANNEL_ID,
            
            messageType: 'link',
            contentActionLabel: 'more',
            contentActionLink: env.BUILD_URL,
            notification: 'Changes have been deployed.'
        )
    }
}
