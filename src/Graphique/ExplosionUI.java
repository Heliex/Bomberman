package Graphique;

import org.newdawn.slick.Animation;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;


public class ExplosionUI {
	
		private Animation milieuExplosion;
		
		private Animation extremiteHaute;
		private Animation explosionMilieuVerticale;
		private Animation extremiteBasse;
		
		private Animation extremiteDroite;
		private Animation explosionMilieuHorizontale;
		private Animation extremiteGauche;
		
		private SpriteSheet sheet;
		private boolean looping;
		private int duration;
		public static final int LARGEUR_SPRITE = 16, HAUTEUR_SPRITE=16;
		
		public ExplosionUI(boolean loop, int duration)
		{
			this.looping = loop;
			this.duration = duration;
			try {
				this.sheet = new SpriteSheet("images/Explosions.png",LARGEUR_SPRITE,HAUTEUR_SPRITE);
				
				// Animation du milieu de l'explosion
				this.milieuExplosion = new Animation();
				this.milieuExplosion.addFrame(sheet.getSubImage(0, 0,LARGEUR_SPRITE,HAUTEUR_SPRITE), duration);
				this.milieuExplosion.addFrame(sheet.getSubImage(16, 0,LARGEUR_SPRITE,HAUTEUR_SPRITE), duration);
				this.milieuExplosion.addFrame(sheet.getSubImage(32, 0,LARGEUR_SPRITE,HAUTEUR_SPRITE), duration);
				this.milieuExplosion.addFrame(sheet.getSubImage(48, 0,LARGEUR_SPRITE,HAUTEUR_SPRITE), duration);
				this.milieuExplosion.addFrame(sheet.getSubImage(64, 0,LARGEUR_SPRITE,HAUTEUR_SPRITE), duration);
				this.milieuExplosion.setLooping(loop);
				
				// Animation de l'extremité haute de l'explosion
				this.extremiteHaute = new Animation();
				this.extremiteHaute.addFrame(sheet.getSubImage(0, 96,LARGEUR_SPRITE,HAUTEUR_SPRITE), duration);
				this.extremiteHaute.addFrame(sheet.getSubImage(16, 96,LARGEUR_SPRITE,HAUTEUR_SPRITE), duration);
				this.extremiteHaute.addFrame(sheet.getSubImage(32, 96,LARGEUR_SPRITE,HAUTEUR_SPRITE), duration);
				this.extremiteHaute.addFrame(sheet.getSubImage(48, 96,LARGEUR_SPRITE,HAUTEUR_SPRITE), duration);
				this.extremiteHaute.addFrame(sheet.getSubImage(64, 96,LARGEUR_SPRITE,HAUTEUR_SPRITE), duration);
				this.extremiteHaute.setLooping(loop);
				
				// Animation du milieu vertical de l'explosion
				this.explosionMilieuVerticale = new Animation();
				this.explosionMilieuVerticale.addFrame(sheet.getSubImage(0, 80,LARGEUR_SPRITE,HAUTEUR_SPRITE), duration);
				this.explosionMilieuVerticale.addFrame(sheet.getSubImage(16, 80,LARGEUR_SPRITE,HAUTEUR_SPRITE), duration);
				this.explosionMilieuVerticale.addFrame(sheet.getSubImage(32, 80,LARGEUR_SPRITE,HAUTEUR_SPRITE), duration);
				this.explosionMilieuVerticale.addFrame(sheet.getSubImage(48, 80,LARGEUR_SPRITE,HAUTEUR_SPRITE), duration);
				this.explosionMilieuVerticale.addFrame(sheet.getSubImage(64, 80,LARGEUR_SPRITE,HAUTEUR_SPRITE), duration);
				this.explosionMilieuVerticale.setLooping(loop);
				
				// Animation de l'extremité basse de l'explosion
				this.extremiteBasse = new Animation();
				this.extremiteBasse.addFrame(sheet.getSubImage(0, 128,LARGEUR_SPRITE,HAUTEUR_SPRITE), duration);
				this.extremiteBasse.addFrame(sheet.getSubImage(16, 128,LARGEUR_SPRITE,HAUTEUR_SPRITE), duration);
				this.extremiteBasse.addFrame(sheet.getSubImage(32, 128,LARGEUR_SPRITE,HAUTEUR_SPRITE), duration);
				this.extremiteBasse.addFrame(sheet.getSubImage(48, 128,LARGEUR_SPRITE,HAUTEUR_SPRITE), duration);
				this.extremiteBasse.addFrame(sheet.getSubImage(64, 128,LARGEUR_SPRITE,HAUTEUR_SPRITE), duration);
				this.extremiteBasse.setLooping(loop);
				
				// Animation de l'extremité droite de l'explosion
				this.extremiteDroite = new Animation();
				this.extremiteDroite.addFrame(sheet.getSubImage(0, 64,LARGEUR_SPRITE,HAUTEUR_SPRITE), duration);
				this.extremiteDroite.addFrame(sheet.getSubImage(16, 64,LARGEUR_SPRITE,HAUTEUR_SPRITE), duration);
				this.extremiteDroite.addFrame(sheet.getSubImage(32, 64,LARGEUR_SPRITE,HAUTEUR_SPRITE), duration);
				this.extremiteDroite.addFrame(sheet.getSubImage(48, 64,LARGEUR_SPRITE,HAUTEUR_SPRITE), duration);
				this.extremiteDroite.addFrame(sheet.getSubImage(64, 64,LARGEUR_SPRITE,HAUTEUR_SPRITE), duration);
				this.extremiteDroite.setLooping(loop);
				
				// Animation du milieu horizontale de l'explosion
				this.explosionMilieuHorizontale = new Animation();
				this.explosionMilieuHorizontale.addFrame(sheet.getSubImage(0, 16,LARGEUR_SPRITE,HAUTEUR_SPRITE), duration);
				this.explosionMilieuHorizontale.addFrame(sheet.getSubImage(16, 16,LARGEUR_SPRITE,HAUTEUR_SPRITE), duration);
				this.explosionMilieuHorizontale.addFrame(sheet.getSubImage(32, 16,LARGEUR_SPRITE,HAUTEUR_SPRITE), duration);
				this.explosionMilieuHorizontale.addFrame(sheet.getSubImage(48, 16,LARGEUR_SPRITE,HAUTEUR_SPRITE), duration);
				this.explosionMilieuHorizontale.addFrame(sheet.getSubImage(64, 16,LARGEUR_SPRITE,HAUTEUR_SPRITE), duration);
				this.explosionMilieuHorizontale.setLooping(loop);
				
				// Animation du milieu horizontale de l'explosion
				this.extremiteGauche = new Animation();
				this.extremiteGauche.addFrame(sheet.getSubImage(0, 32,LARGEUR_SPRITE,HAUTEUR_SPRITE), duration);
				this.extremiteGauche.addFrame(sheet.getSubImage(16, 32,LARGEUR_SPRITE,HAUTEUR_SPRITE), duration);
				this.extremiteGauche.addFrame(sheet.getSubImage(32, 32,LARGEUR_SPRITE,HAUTEUR_SPRITE), duration);
				this.extremiteGauche.addFrame(sheet.getSubImage(48, 32,LARGEUR_SPRITE,HAUTEUR_SPRITE), duration);
				this.extremiteGauche.addFrame(sheet.getSubImage(64, 32,LARGEUR_SPRITE,HAUTEUR_SPRITE), duration);
				this.extremiteGauche.setLooping(loop);
				
			} catch (SlickException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		
		public Animation getMilieuExplosion()
		{
			return this.milieuExplosion;
		}
		
		public Animation getExtremiteHauteExplosion()
		{
			return this.extremiteHaute;
		}
		
		public Animation getMilieuVerticalExplosion()
		{
			return this.explosionMilieuVerticale;
		}
		
		public Animation getExtremiteBasseExplosion()
		{
			return this.extremiteBasse;
		}
		
		public Animation getExtremiteDroiteExplosion()
		{
			return this.extremiteDroite;
		}
		
		public Animation getMilieuHorizontalExplosion()
		{
			return this.explosionMilieuHorizontale;
		}
		
		public Animation getExtremiteGaucheExplosion()
		{
			return this.extremiteGauche;
		}
		
		public int getDuration()
		{
			return this.duration;
		}
		
		public boolean isLooping()
		{
			return this.looping;
		}
}
