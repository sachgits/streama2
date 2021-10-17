package streama


import grails.rest.*
import grails.converters.*
import java.util.UUID;

class SignupController {

    def index() {

    }
     def register(User userInstance){
       if(userInstance == null){
         render status: NOT_FOUND
         return
       }

       userInstance.validate();
       if(userInstance.hasErrors()){
         render status: NOT_ACCEPTABLE
       }
       userInstance.uuid = randomUUID() as String
       userInstance.save flush: true
       respond userInstance, [status: CREATED]


     }

  def createAndSaveUser(){
    return  !kaswitch;
  }
}
