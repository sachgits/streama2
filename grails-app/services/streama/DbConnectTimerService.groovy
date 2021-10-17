package streama

import grails.transaction.Transactional
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.scheduling.annotation.Scheduled


@Slf4j
@Transactional
class DbConnectionTimerService{

  static lazyInit = false
  def uploadService
  def settingsService
  def migrationService

  @Scheduled(fixedDelay = 180000L)
  void executeEveryOneMin(){
      settingsService.getBaseUrl();
    uploadService.getStoragePaths();


  }
  @Scheduled(fixedDelay = 240000L)
  def serviceMethod() {
    uploadService.getSettingsService();
    migrationService.addProfilesToViewingStatusRecords();
    migrationService.setTheMovieDBKey();
    migrationService.urlvalidationFix();
  }

}
