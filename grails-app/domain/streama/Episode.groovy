package streama

class Episode extends Video{

  transient theMovieDbService

  String name
  String air_date
  int season_number
  int episode_number
  Integer seasonEpisodeMerged
  String episodeString

  String still_path

  TvShow show
  File still_image

  static constraints = {
  }

  static mapping = {
    cache true
    show cache: true
  }

  def beforeUpdate(){
    episodeString = "s" + season_number.toString().padLeft(2, '0') + "e" + episode_number.toString().padLeft(2, '0')
    createMergedSeasonEpisode()
  }

  def getMovieDbMeta(){
    theMovieDbService.getEpisodeMeta(this.show.apiId, this.season_number, this.episode_number)
  }

  void createMergedSeasonEpisode(){
    seasonEpisodeMerged = "${season_number + 1000}${episode_number + 1000}".toInteger()
  }
}
