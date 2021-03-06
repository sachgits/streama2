package streama

import grails.transaction.Transactional
import grails.web.servlet.mvc.GrailsParameterMap

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE

@Transactional
class PlayerService {

    def springSecurityService
    def kbSubscriptionService
    def kbAccountService
    def kbUsageService

    @Transactional
    def updateViewingStatus(GrailsParameterMap params) {
        User currentUser = springSecurityService.currentUser
        Video video = Video.get(params.getInt('videoId'))
        Double currentTime = params.getDouble('currentTime');
        Double runtime = params.getDouble('runtime');
        ViewingStatus viewingStatus

        if (!video || currentTime == null) {
            return ResultHelper.generateErrorResult(NOT_ACCEPTABLE, 'video_currentTime', 'video or currentTime missing')
        }

        if(video instanceof Episode){
            viewingStatus = ViewingStatus.findOrCreateByTvShowAndUser(video.show, currentUser)
            viewingStatus.tvShow = video.show
        }else{
            viewingStatus = ViewingStatus.findOrCreateByVideoAndUser(video, currentUser)
        }

        viewingStatus.video = video
        viewingStatus.currentPlayTime = currentTime
        viewingStatus.runtime = runtime
        viewingStatus.user = currentUser
        viewingStatus.profile = params.profile


        viewingStatus.validate()
        if (viewingStatus.hasErrors()) {
            return ResultHelper.generateErrorResult(NOT_ACCEPTABLE, 'viewingStatus', 'viewingStatus validate')
        }

        viewingStatus.save flush:true
        //TODO: when this is not working WE GET NO MONEY and User KEEPS ON wWatching!!!!....
        def userAcc = kbAccountService.getUserAccountByKey(currentUser.getUsername());
        def subscription = kbSubscriptionService.getUserSubscriptions(currentUser);
        kbUsageService.recordSubscriptionUsage(subscription.getSubscriptionId());
        log.info("something was sent to killbill usage");
        log.info("hopping it works");

        return ResultHelper.generateOkResult(CREATED, viewingStatus)
    }

}
