package streama


class VideoInterceptor {

def kbSubscriptionService
def springSecurityService

    public VideoInterceptor(){
    match controller: 'video', action: 'show'
    }

    boolean before() {
      User user =springSecurityService.currentUser;
      def planName = "wiflix-fixed-daily"

      if(kbSubscriptionService.userGotCashForPlan(user,planName)){
              return true;
      }
      redirect controller: 'dash', action: 'listMovies'
      return false;
    }

    boolean after() { true }

    void afterView() {
        // no-op
    }
}
