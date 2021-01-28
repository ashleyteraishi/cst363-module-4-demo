package edu.cst363;

public class Rating {
	   String name;
	   String item;
	   int  stars;

	   public Rating() {
	    item="";
	    stars=0;
	   }

	   public Rating(String name, String item, int stars) {
		 this.name=name;
	     this.item = item;
	     this.stars = stars;
	   }
	   
	   public String getName() { return name; }
	   public void setName(String name) { this.name = name; }
	   public String getItem() { return item; }
	   public void setItem(String item) { this.item=item; }
	   public int getStars() { return stars; }
	   public void setStars(int stars) { this.stars=stars; }
	   public String getStarsString() { 
	     String r = "";
	     for (int i=0; i<stars; i++) {
	       //  code point for star symbol Unicode
	       r = r + "\u2606"; 
	     }
	     return r;
	   }
	}
