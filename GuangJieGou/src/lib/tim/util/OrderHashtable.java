package lib.tim.util;

import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

public class OrderHashtable<K, V> extends Hashtable<K, V>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<V> list;
	
	public OrderHashtable(){
		list=new Vector<V>();
	}
	
	public synchronized V insert(K key, V value,int location){
		V oldV=super.put(key, value);
		if(oldV!=null){
			list.remove(oldV);
		}
		list.add(location, value);
		return oldV;
	}
	
	public synchronized V put (K key, V value){
		V oldV=super.put(key, value);
		if(oldV!=null){
			list.remove(oldV);
		}
		list.add(value);
		return oldV;
	}
	
	public synchronized V remove (Object key){
		V oldV=super.remove(key);
		if(oldV!=null){
			list.remove(oldV);
		}
		return oldV;
	}
	
	public List<V> getOrderList(){
		return list;
	}
	
	public synchronized void clear (){
		super.clear();
		list.clear();
	}
	
}
