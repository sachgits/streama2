package streama

import org.grails.web.util.WebUtils

import static java.util.UUID.randomUUID

class User {

	transient springSecurityService
	transient settingsService

	Date dateCreated
	Date lastUpdated
  boolean deleted = false

	String username
	String password

	boolean enabled = false
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired
	boolean invitationSent = false
	boolean pauseVideoOnClick = true
	String uuid
	String language = 'en'
  String fullName
  Integer amountOfMediaEntries

	static transients = ['springSecurityService']

  static hasMany = [favoriteGenres: Genre, profiles: Profile]

	static constraints = {
		username blank: false, unique: true
		password blank: false
		dateCreated nullable: true
		lastUpdated nullable: true
	}

	static mapping = {
		password column: '`password`'
		cache true
	}

	Set<Role> getAuthorities() {
		UserRole.findAllByUser(this).collect { it.role }
	}

	def beforeInsert() {
        if(!uuid){
            uuid = randomUUID() as String
        }
		if(!password){
			password  = randomUUID() as String
		}

		encodePassword()
	}

	def beforeUpdate() {
		if (isDirty('password')) {
			encodePassword()
		}
	}

	def getInvitationLink(){
		if(invitationSent && uuid){
			return settingsService.baseUrl +  "/invite?uuid=${uuid}"
		}
	}

	protected void encodePassword() {
		password = springSecurityService?.passwordEncoder ? springSecurityService.encodePassword(password) : password
	}

  static Profile getProfileFromRequest(){
    def request = WebUtils.retrieveGrailsWebRequest()?.getCurrentRequest()
    if(!request){
      return
    }
    def profileId = request.getHeader("profileId")
    def currentProfile = Profile.get(profileId)

    return currentProfile
  }
}
