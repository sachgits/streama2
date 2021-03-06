package streama

import grails.converters.JSON

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class GenreController {

    static responseFormats = ['json', 'xml']
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Genre.list(params), [status: OK]
    }

    @Transactional
    def save(Genre genreInstance) {
        if (genreInstance == null) {
            render status: NOT_FOUND
            return
        }

        genreInstance.validate()
        if (genreInstance.hasErrors()) {
            render status: NOT_ACCEPTABLE
            return
        }

        genreInstance.save flush:true
        respond genreInstance, [status: CREATED]
    }

    @Transactional
    def update(Genre genreInstance) {
        if (genreInstance == null) {
            render status: NOT_FOUND
            return
        }

        genreInstance.validate()
        if (genreInstance.hasErrors()) {
            render status: NOT_ACCEPTABLE
            return
        }

        genreInstance.save flush:true
        respond genreInstance, [status: OK]
    }

    @Transactional
    def delete(Genre genreInstance) {

        if (genreInstance == null) {
            render status: NOT_FOUND
            return
        }

        genreInstance.delete flush:true
        render status: NO_CONTENT
    }

    def show(Genre genreInstance) {
        respond genreInstance, [status: OK]
    }
}
