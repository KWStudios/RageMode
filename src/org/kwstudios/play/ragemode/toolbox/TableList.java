package org.kwstudios.play.ragemode.toolbox;

import java.util.ArrayList;
import java.util.List;

public class TableList<S1, S2> {
	
	List<S1> firstList;
	List<S2> secondList;
	
	public TableList(){
		createLists();
	}
	
	public void setToFirstObject(int index, S1 element){
		firstList.set(index, element);
	}
	
	public void addToFirstObject(S1 e){
		firstList.add(e);
	}
	
	public S1 getFromFirstObject(int index){
		return firstList.get(index);
	}
	
	public List<S1> getFirstList(){
		return firstList;
	}
	
	public int getFirstLength(){
		return firstList.size();
	}
	
	public void setToSecondObject(int index, S2 element){
		secondList.set(index, element);
	}
	
	public void addToSecondObject(S2 e){
		secondList.add(e);
	}
	
	public S2 getFromSecondObject(int index){
		return secondList.get(index);
	}
	
	public List<S2> getSecondList(){
		return secondList;
	}
	
	public int getSecondLength(){
		return secondList.size();
	}
	
	public void setToBoth(int index1, S1 element1, int index2, S2 element2){
		firstList.set(index1, element1);
		secondList.set(index2, element2);
	}
	
	public void addToBoth(S1 e1, S2 e2){
		firstList.add(e1);
		secondList.add(e2);
	}
	
	public void removeFromBoth(int index){
		firstList.remove(index);
		secondList.remove(index);
	}
	
	public int getLength(){
		if(firstList.size() >= secondList.size()){
			return firstList.size();
		}else{
			return secondList.size();
		}
	}
	
	private void createLists(){
		firstList = new ArrayList<S1>();
		secondList = new ArrayList<S2>();
	}

}
