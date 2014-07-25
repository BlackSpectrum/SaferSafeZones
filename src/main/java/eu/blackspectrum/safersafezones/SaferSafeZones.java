package eu.blackspectrum.safersafezones;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.massivecore.ps.PS;

public class SaferSafeZones extends JavaPlugin implements Listener
{


	@Override
	public void onEnable() {

		Bukkit.getPluginManager().registerEvents( this, this );
	}




	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerChangedWorld( final PlayerChangedWorldEvent event ) {
		final Player player = event.getPlayer();

		if ( this.isInSafeZone( player.getLocation() ) )
			this.onEnterSafeZone( player );
	}




	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerChunkMove( final PlayerMoveEvent event ) {
		if ( event.isCancelled() )
			return;

		final Location from = event.getFrom();
		final Location to = event.getTo();
		final Player player = event.getPlayer();

		if ( from.getChunk().equals( to.getChunk() ) )
			return;

		if ( this.isInSafeZone( to ) )
			this.onEnterSafeZone( player );
	}




	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerConsume( final PlayerItemConsumeEvent event ) {
		if ( event.getItem().getType().equals( Material.POTION ) )
			event.setCancelled( true );
	}




	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerDamaged( final EntityDamageEvent event ) {
		if ( event.getEntityType().equals( EntityType.PLAYER ) )
			if ( this.isInSafeZone( event.getEntity().getLocation() ) )
				event.setCancelled( true );
	}




	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoined( final PlayerJoinEvent event ) {
		final Player player = event.getPlayer();

		if ( this.isInSafeZone( player.getLocation() ) )
			this.onEnterSafeZone( player );
	}




	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerUseItem( final PlayerInteractEvent event ) {
		if ( this.isInSafeZone( event.getPlayer().getLocation() ) )
		{
			final Material item = event.getItem().getType();

			if ( item.equals( Material.EGG ) || item.equals( Material.ENDER_PEARL ) || item.equals( Material.POTION ) )
				event.setCancelled( true );
		}
	}




	private boolean isInSafeZone( final Location location ) {
		return BoardColls.get().getFactionAt( PS.valueOf( location ) ).getName().equalsIgnoreCase( "SafeZone" );
	}




	private void onEnterSafeZone( final Player player ) {
		for ( final PotionEffect pot : player.getActivePotionEffects() )
			player.removePotionEffect( pot.getType() );
	}

}
