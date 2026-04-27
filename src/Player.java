public class Player implements Comparable<Player> {
    private String nickname;
    private int ranking;

    public Player(String nickname, int ranking) {
        this.nickname = nickname;
        this.ranking = ranking;
    }

    public String getNickname() { return nickname; }
    public int getRanking() { return ranking; }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    @Override
    public int compareTo(Player other) {
        return Integer.compare(this.ranking, other.ranking);
    }

    @Override
    public String toString() {
        return nickname + " (#" + ranking + ")";
    }
}
