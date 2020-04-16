import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Player {

	private int payoff = 0;
	private int strategy = -1;
	private int previousMove = -2;

	Player(int payoff, int strategy, int previousMove) {
		this.payoff = payoff;
		this.strategy = strategy;
		this.previousMove = previousMove;
	}

	int getP() {
		return this.payoff;
	}

	void setP(int p) {
		this.payoff = p;
	}

	int getS() {
		return this.strategy;
	}

	void setS(int s) {
		this.strategy = s;
	}

	int getPM() {
		return this.previousMove;
	}

	void setPM(int pm) {
		this.previousMove = pm;
	}

	public static void main(String[] args) {
		int n = 100;
		int m = 5;
		int k = 20;
		int p = 5;

		List<Player> players = new ArrayList<Player>();
		
		for (int x = 0; x < n; x++) {
			int strategy = 0;
			if (x<25) {
				strategy = 0;
			}
			else if (x<50) {
				strategy = 1;
			}
			else if (x<75){
				strategy = 2;
			} else {
				strategy = 3;
			}
			Player s = new Player(0, strategy, -1);
			players.add(s);
		}

		for (int k1 = 0; k1 < k; k1++) {
			for (int y = 0; y < n - 1; y++) {
				Player player = players.get(y);
				for (int w = y + 1; w < n; w++) {
					Player vsplayer = players.get(w);
					for (int x1 = 0; x1 < m; x1++) {
						int pmove = 0;
						int vsmove = 0;
						if (player.getS() == 0) {
							if (vsplayer.getPM() == -1) {
								pmove = 1;
							} else {
								pmove = vsplayer.getPM();
							}
						} else if (player.getS() == 1) {
							if (player.getPM() == -1 || player.getPM() == 1) {
								if (vsplayer.getPM() == 0) {
									pmove = 0;
								} else {
									pmove = 1;
								}
							} else {
								pmove = 0;
							}
						} else if (player.getS() == 2) {
							pmove = 1;
						} else {
							pmove = 0;
						}

						if (vsplayer.getS() == 0) {
							if (player.getPM() == -1) {
								vsmove = 1;
							} else {
								vsmove = player.getPM();
							}
						} else if (vsplayer.getS() == 1) {
							if (vsplayer.getPM() == -1 || vsplayer.getPM() == 1) {
								if (player.getPM() == 0) {
									vsmove = 0;
								} else {
									vsmove = 1;
								}
							} else {
								vsmove = 0;
							}
						} else if (vsplayer.getS() == 2) {
							vsmove = 1;
						} else {
							vsmove = 0;
						}

						if (pmove == 1 && vsmove == 1) {
							player.setP(player.getP() + 3);
							vsplayer.setP(vsplayer.getP() + 3);
						} else if (pmove == 1 && vsmove == 0) {
							vsplayer.setP(vsplayer.getP() + 5);
						} else if (pmove == 0 && vsmove == 1) {
							player.setP(player.getP() + 5);
						} else {
							player.setP(player.getP() + 1);
							vsplayer.setP(vsplayer.getP() + 1);
						}

						player.setPM(pmove);
						vsplayer.setPM(vsmove);
					}
					player.setPM(-1);
					vsplayer.setPM(-1);
				}
			}

			for (int x = 0; x < players.size(); x++) {
				Player pcurrent = players.get(x);
				int largestP = x;
				for (int y = x + 1; y < players.size(); y++) {
					if (players.get(y).getP() > players.get(largestP).getP()) {
						largestP = y;
					}
				}
				players.set(x, players.get(largestP));
				players.set(largestP, pcurrent);
			}

			int T4T = 0;
			int G = 0;
			int AC = 0;
			int AD = 0;

			int T4Ttotal = 0;
			int Gtotal = 0;
			int ACtotal = 0;
			int ADtotal = 0;

			for (Player p1 : players) {
				if (p1.getS() == 0) {
					T4T++;
					T4Ttotal += p1.getP();
				} else if (p1.getS() == 1) {
					G++;
					Gtotal += p1.getP();
				} else if (p1.getS() == 2) {
					AC++;
					ACtotal += p1.getP();
				} else {
					AD++;
					ADtotal += p1.getP();
				}
			}
			System.out.println(
					"Gen " + (k1 + 1) + ": \tT4T: " + T4T + "%\tG: " + G + "%\tAC: " + AC + "%\tAD: " + AD + "%");

			int total = T4Ttotal + Gtotal + ACtotal + ADtotal;

			System.out.println("Gen " + (k1 + 1) + ": \t" + T4Ttotal + "\t" + Gtotal + "\t" + ACtotal + "\t" + ADtotal+ "\tTotal:" + total);

			double T4Tavg = ((int) ((T4Ttotal / (double) T4T) * 10)) / 10.0;
			double Gavg = ((int) ((Gtotal / (double) G) * 10)) / 10.0;
			double ACavg = ((int) ((ACtotal / (double) AC) * 10)) / 10.0;
			double ADavg = ((int) ((ADtotal / (double) AD) * 10)) / 10.0;

			System.out.println("Gen " + (k1 + 1) + ": \t" + T4Tavg + "\t" + Gavg + "\t" + ACavg + "\t" + ADavg);

			System.out.println();

			List<Player> newplayers = new ArrayList<Player>();
			for (int a = 0; a < n - p; a++) {

				Player s = players.get(a);
				s.setP(0);
				s.setPM(-1);

				if (a < p) {
					Player p1 = new Player(0, s.getS(), -1);
					newplayers.add(s);
					newplayers.add(p1);
				} else {
					newplayers.add(s);
				}
			}

			players = new LinkedList<Player>();
			Random random = new Random();
			while (!newplayers.isEmpty()) {
				int index = random.nextInt(newplayers.size());
				players.add(newplayers.remove(index));
			}
		}
	}
}
