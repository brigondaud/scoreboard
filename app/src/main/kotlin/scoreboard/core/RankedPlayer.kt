package scoreboard.core

class RankedPlayer(player: Player, val rank: Int) : Player(player.pseudo, player.score)