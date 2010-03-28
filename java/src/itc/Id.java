package itc;

import util.BitArray;

public class Id {

	protected boolean isLeaf;
	private int value;
	private Id left;
	private Id right;

	public Id() {
		this.value = 1;
		this.isLeaf = true;
		this.left = null;
		this.right = null;
	}

	public Id(int val) {
		this.value = val;
		this.isLeaf = true;
		this.left = null;
		this.right = null;
	}

	public Id(Id i) {
		this.isLeaf = i.isLeaf;
		this.value = i.getValue();
		this.left = i.getLeft();
		this.right = i.getRight();
	}

	public Id split() { // split shall return the right hand id of the result, while making himself the left hand id
		Id i2 = new Id();

		if (this.isLeaf && this.value == 0) { // id = 0
			this.setAsLeaf();
			this.value = 0;

			i2.setAsLeaf();
			i2.setValue(0);
		} else if (this.isLeaf && this.value == 1) { // id = 1
			this.setAsNode();
			this.value = 0;
			this.left = new Id(1);
			this.right = new Id(0);

			i2.setAsNode();
			i2.setLeft(new Id(0));
			i2.setRight(new Id(1));
		} else {
			if (this.isLeaf == false && (this.left.isLeaf && this.left.getValue() == 0) && (this.right.isLeaf == false || this.right.getValue() == 1)) { // id = (0, i)
				this.setAsNode();
				this.value = 0;
				this.left = new Id(0);

				i2.setAsNode();
				i2.setLeft(new Id(0));
				i2.setRight(this.right.split());
			} else if (this.isLeaf == false && (this.left.isLeaf == false || this.left.getValue() == 1) && (this.right.isLeaf && this.right.getValue() == 0)) { // id = (i, 0)
				this.setAsNode();
				this.value = 0;
				this.right = new Id(0);

				i2.setAsNode();
				i2.setLeft(this.left.split());
				i2.setRight(new Id(0));
			} else if (this.isLeaf == false && (this.left.isLeaf == false || this.left.getValue() == 1) && (this.right.isLeaf == false || this.right.getValue() == 1)) { // id = (i1, i2)
				i2.setAsNode();
				i2.setRight(this.right);
				i2.setLeft(new Id(0));

				this.setAsNode();
				this.value = 0;
				// this.i1 = this.i1;
				this.right = new Id(0);
			} else {
				System.out.println("Bug..." + this.toString());
			}
		}
		return i2;
	}

	public Id[] split2() {
		Id i1 = new Id();
		Id i2 = new Id();

		if (this.isLeaf && this.value == 0) { // id = 0
			System.out.println("ID = 0??? FUCK");
			i1.setAsLeaf();
			i1.setValue(0);

			i2.setAsLeaf();
			i2.setValue(0);
		} else if (this.isLeaf && this.value == 1) { // id = 1
			i1.setAsNode();
			i1.setValue(0);
			i1.setLeft(new Id(1));
			i1.setRight(new Id(0));

			i2.setAsNode();
			i2.setValue(0);
			i2.setLeft(new Id(0));
			i2.setRight(new Id(1));
		} else {
			if (this.isLeaf == false && (this.left.isLeaf && this.left.getValue() == 0) && (this.right.isLeaf == false || this.right.getValue() == 1)) { // id = (0, i)
				Id[] ip = this.right.split2();

				i1.setAsNode();
				i1.setValue(0);
				i1.setLeft(new Id(0));
				i1.setRight(ip[0]);

				i2.setAsNode();
				i2.setValue(0);
				i2.setLeft(new Id(0));
				i2.setRight(ip[1]);
			} else if (this.isLeaf == false && (this.left.isLeaf == false || this.left.getValue() == 1) && (this.right.isLeaf && this.right.getValue() == 0)) { // id = (i, 0)
				Id[] ip = this.left.split2();

				i1.setAsNode();
				i1.setValue(0);
				i1.setLeft(ip[0]);
				i1.setRight(new Id(0));

				i2.setAsNode();
				i2.setValue(0);
				i2.setLeft(ip[1]);
				i2.setRight(new Id(0));
			} else if (this.isLeaf == false && (this.left.isLeaf == false || this.left.getValue() == 1) && (this.right.isLeaf == false || this.right.getValue() == 1)) { // id = (i1, i2)
				i1.setAsNode();
				i1.setValue(0);
				i1.setLeft(this.left.clone());
				i1.setRight(new Id(0));

				i2.setAsNode();
				i2.setValue(0);
				i2.setLeft(new Id(0));
				i2.setRight(this.right.clone());
			} else {
				System.out.println("Bug..." + this.toString());
			}
		}

		Id[] res = new Id[2];
		res[0] = i1;
		res[1] = i2;
		return res;
	}

	public void sum(Id i1, Id i2) { // this becomes the sum between i1 and i2

		this.left = new Id();
		this.right = new Id();

		if (i1.isLeaf && i1.getValue() == 0 && i2.isLeaf && i2.getValue() == 0) {
			this.setAsLeaf();
			this.value = 0;
		} else if (i1.isLeaf && i1.getValue() == 0 && (i2.getValue() == 1 || i2.isLeaf == false)) { // sum(0, X) -> X;
			if (i2.isLeaf) {
				this.setAsLeaf();
			} else if (i2.isLeaf == false) {
				this.setAsNode();
			}
			this.value = i2.getValue();
			this.left = i2.getLeft();
			this.right = i2.getRight();
		} else if ((i1.getValue() == 1 || i1.isLeaf == false) && i2.isLeaf && i2.getValue() == 0) { // sum(X, 0) -> X;
			if (i1.isLeaf) {
				this.setAsLeaf();
			} else if (i1.isLeaf == false) {
				this.setAsNode();
			}
			this.value = i1.getValue();
			this.left = i1.getLeft();
			this.right = i1.getRight();
		} else if (i1.isLeaf == false && i2.isLeaf == false) { // sum({L1,R1}, {L2, R2}) -> norm_id({sum(L1, L2), sum(R1, R2)}).
			this.setAsNode();
			this.left.sum(i1.getLeft(), i2.getLeft());
			this.right.sum(i1.getRight(), i2.getRight());
			this.normalize();
		} else {
			System.out.println("fail Id ..." + this.toString());
			System.out.println("flail heck ..." + i1.getValue() + " " + i2.getValue());
		}// else do nothing
	}

	public void normalize() {
		if (this.isLeaf == false && this.left.isLeaf && this.left.getValue() == 0 && this.right.isLeaf && this.right.getValue() == 0) {
			this.setAsLeaf();
			this.value = 0;
			this.left = this.right = null;
		} else if (this.isLeaf == false && this.left.isLeaf && this.left.getValue() == 1 && this.right.isLeaf && this.right.getValue() == 1) {
			this.setAsLeaf();
			this.value = 1;
			this.left = this.right = null;
		}// else do nothing
	}

	public char[] dEncode() {
		return this.encode(null).unify();
	}

	// code and decode dos ids
	public BitArray encode(BitArray bt) {
		if (bt == null) {
			bt = new BitArray();
		}

		if (this.isLeaf && this.value == 0) {//System.out.println("id enc a1");
			bt.addbits(0, 3);
		} else if (this.isLeaf && this.value == 1) {//System.out.println("id enc a2");
			bt.addbits(0, 2);
			bt.addbits(1, 1);
		} else if (this.isLeaf == false && (this.left.isLeaf && this.left.getValue() == 0) && (this.right.isLeaf == false || this.right.getValue() == 1)) {//System.out.println("id enc b");
			bt.addbits(1, 2);
			this.right.encode(bt);
		} else if (this.isLeaf == false && (this.right.isLeaf && this.right.getValue() == 0) && (this.left.isLeaf == false || this.left.getValue() == 1)) {//System.out.println("id enc c");
			bt.addbits(2, 2);
			this.left.encode(bt);
		} else if (this.isLeaf == false && (this.right.isLeaf == false || this.right.getValue() == 1) && (this.left.isLeaf == false || this.left.getValue() == 1)) {//System.out.println("id enc d");
			bt.addbits(3, 2);
			this.left.encode(bt);
			this.right.encode(bt);
		} else {
			System.out.println("BUG - ENCODE");
			System.out.println("this tipo " + this.isLeaf);
			System.out.println(" i1 tipo is Leaf?" + this.left.isLeaf);
			System.out.println(" i2 tipo is Leaf?" + this.right.isLeaf);
		}
		//System.out.println(" i2 tipo " + (int)bt.getIndex(0));
		return bt;
	}

	public void decode(BitArray bt) {
		int val = bt.readbits(2);
		if (val == 0) {//System.out.println("Id dec a");
			int x = bt.readbits(1);//printf("chceck\n");

			this.setAsLeaf();
			this.value = x;
		} else if (val == 1) {//System.out.println("Id dec b");
			this.setAsNode();

			this.left = new Id(0);
			this.right = new Id();
			this.right.decode(bt);
		} else if (val == 2) {//System.out.println("Id dec c");
			this.setAsNode();

			this.left = new Id();
			this.left.decode(bt);
			this.right = new Id(0);
		} else if (val == 3) {//System.out.println("Id dec d");
			this.setAsNode();

			this.left = new Id();
			this.left.decode(bt);
			this.right = new Id();
			this.right.decode(bt);
		} else {
			System.out.println("BUG - DECODE");
		}
	}

	// gets, sets e outros
	public void setAsLeaf() {
		this.isLeaf = true;
		this.left = null;
		this.right = null;
	}

	public void setAsNode() {
		this.isLeaf = false;
		this.value = -1;
		this.left = new Id(1);
		this.right = new Id(0);
	}

	public void setValue(int v) {
		this.value = v;
	}

	public void setLeft(Id ni) {
		this.left = ni;
	}

	public void setRight(Id ni) {
		this.right = ni;
	}

	public int getValue() {
		return this.value;
	}

	public Id getLeft() {
		if (this.left != null) {
			return this.left.clone();
		} else {
			return null;
		}
	}

	public Id getRight() {
		if (this.right != null) {
			return this.right.clone();
		} else {
			return null;
		}
	}

	@Override
	public String toString() {

		if (this.isLeaf) {
			return this.value + "";
		} else {
			return "(" + this.left.toString() + ", " + this.right.toString() + ")";
		}
	}

	@Override
	public Id clone() {
		Id res = new Id();

		res.isLeaf = this.isLeaf;
		res.setValue(this.value);
		res.setLeft(this.getLeft());
		res.setRight(this.getRight());
		return res;
	}
}
