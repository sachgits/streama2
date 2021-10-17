package streama

import eu.bitwalker.useragentutils.UserAgent
import grails.transaction.Transactional

import javax.servlet.http.HttpServletRequest

@Transactional
class UserActivityService {
    def springSecurityService
    def settingsService
    def kbAccountService
    def kbSubscriptionService
    def kbUsageService

    def createActivityEntry(HttpServletRequest request, String type, Video video = null) {
      UserActivity userActivity = new UserActivity()
      def user = springSecurityService.currentUser
      if(type == 'video'){
        userActivity = UserActivity.findOrCreateByUserAndVideo(user, video)
      }

        def userAcc = kbAccountService.getUserAccountByKey(user.getUsername());
        def subscription = kbSubscriptionService.getUserSubscriptions(user);
        kbUsageService.recordSubscriptionUsage(subscription.getSubscriptionId());
        log.info("something was sent to killbill usage: CreateActivityEntry");
        log.info("hopping it works OnCreateActivityEntry");


      UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader('user-agent'))
      userActivity.user = user
      userActivity.ipAddress = request.getHeader('x-forwarded-for') ?: request.remoteHost
      userActivity.operatingSystem = userAgent.operatingSystem.name
      userActivity.device = userAgent.operatingSystem.deviceType.name
      userActivity.browser = userAgent.browser.name
      userActivity.video = video
      userActivity.type = type

      userActivity.save(flush: true, failOnError: true)


      def userActivityRotation = settingsService.getValueForName('user_activity_rotation')
      if(userActivityRotation && type == 'login'){
        def allUserActivity = UserActivity.where{
          user == springSecurityService.currentUser
          video == null
        }.list(offset: userActivityRotation, sort: 'id', order: 'DESC')
        if(allUserActivity){
          allUserActivity*.delete(flush: true)
        }
      }


    }
}
