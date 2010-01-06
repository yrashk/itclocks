/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Test;


import causal_histories.*;
import itc.*;
import util.*;

public class Test_itv_vs_ch {



	public static void main(String[] args) {
		(new Test_itv_vs_ch()).CHvsITC();
	}

	public void CHvsITC() {


		// HC mechanism
		Gerador gen = new Gerador();
		Dice dado = new Dice();
		Bag<CStamp> saco = new Bag<CStamp>();

		CStamp seed = new CStamp();
		seed.seed(gen.seed());
		saco.push(seed);

		// ITC mechanism
		Bag<Stamp> bag = new Bag<Stamp>();

		Stamp seedb = new Stamp();
		bag.push(seedb);

		int forks = 0;
		int joins = 0;
		int events = 0;

		int i;
		int counter = 0;
		for (i = 0; i < 1000; i++){
			int tipo = dado.iroll(1, 100);

			if ( tipo <= 34  || saco.getSize() == 1){ // fork
				//System.out.println("Fork __________________________");
				forks++;
				int ind = saco.getValidIndice();

				// mecanismo hc
				CStamp out = (CStamp) saco.popInd(ind);
				CStamp novo = new CStamp();
				novo = out.fork();

				saco.push(out);
				saco.push(novo);

				// mecanismo itc in place
				/*Stamp sout = (Stamp) bag.popInd(ind);
				Stamp sin = new Stamp();
				sin = sout.fork();

				bag.push(sout);
				bag.push(sin);*/

				// mecanismo itc funcional
				Stamp outb = (Stamp) bag.popInd(ind);
				Pair<Stamp> p = outb.fork2();
				Stamp in1 = (Stamp) p.getEa();
				Stamp in2 = (Stamp) p.getEb();

				bag.push(in1);
				bag.push(in2);
			}else if ( tipo <= 66){ // join
				//System.out.println("Join __________________________");
				joins++;
				int inda = saco.getValidIndice();

				CStamp outa = (CStamp) saco.popInd(inda);
				Stamp souta = (Stamp) bag.popInd(inda);

				int indb = saco.getValidIndice();

				CStamp outb = (CStamp) saco.popInd(indb);
				Stamp soutb = (Stamp) bag.popInd(indb);

				CStamp novo = new CStamp();
				novo.join(outa, outb);
				saco.push(novo);

				Stamp novob = new Stamp();
				novob.join(souta, soutb);
				bag.push(novob);
			}else{ // event
				//System.out.println("Event _________________________");
				events++;
				int ind = saco.getValidIndice();

				CStamp out = (CStamp) saco.popInd(ind);
				out.event(gen.gera());
				saco.push(out);

				Stamp outb = (Stamp) bag.popInd(ind);
				Stamp in = new Stamp();
				in = outb.event();
				bag.push(in);
			}

			CStamp tmp = new CStamp();
			tmp = (CStamp) saco.getLast();
			Stamp tmpb = new Stamp();
			tmpb = (Stamp) bag.getLast();
			int len = saco.getSize();

			for (int n = 0; n < len-1; n++){
				boolean a = tmp.leq((CStamp) saco.getInd(n));

				Stamp decd = new Stamp();
				decd.dDecode(bag.getInd(n).dEncode());
				boolean b = tmpb.leq(decd);
				if (!((a && b) || (!a && !b))){
					//System.out.println("Bug " + a + " " + b + "                                                   xxxs");
					//System.out.println(tmp.tostring());
					//System.out.println(((CStamp) saco.getInd(n)).tostring());
					//System.out.println(tmpb.tostring());
					//System.out.println(((Stamp) bag.getInd(n)).tostring());

					counter++;
				}
			}
		}
		System.out.println(" Bugs : "+counter);
		System.out.println("=======================");
		System.out.println(" Forks  : "+forks);
		System.out.println(" Joins  : "+joins);
		System.out.println(" Events : "+events);
		System.out.println("");
		System.out.println(" Bag final size : "+bag.getSize());
	}



}
