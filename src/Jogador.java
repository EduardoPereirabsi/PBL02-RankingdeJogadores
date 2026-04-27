// Classe que representa um jogador com nickname e ranking
// Baseado na estrutura do CSV (nickname, ranking)
public class Jogador implements Comparable<Jogador> {
    private String nickname;
    private int ranking;

    public Jogador(String nickname, int ranking) {
        this.nickname = nickname;
        this.ranking = ranking;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    @Override
    public int compareTo(Jogador outro) {
        return Integer.compare(this.ranking, outro.ranking);
    }

    @Override
    public String toString() {
        return nickname + " (#" + ranking + ")";
    }
}
