package main.java.core;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import main.java.dfs.ChordMessageInterface;
import main.java.dfs.DFS;
import main.java.model.Artist;
import main.java.model.Music;
import main.java.model.Song;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class SearchThread implements Runnable {
	String text;
	String searchResult;
	Long time;
	int counter;
	int page;
	DFS.FileJson file;
	DFS dfs;


	public SearchThread(int page, DFS.FileJson file, DFS dfs, String text, int counter) {
		this.page = page;
		this.file = file;
		this.dfs = dfs;
		this.text = text;
		this.counter = counter;
	}

	public String getResults(){
		return searchResult;
	}

	public int getCounter(){
		return counter;
	}
//	@Override
//	public void run() {
//		counter++;
//		List<Music> music = null;
//		Long pageGuid = this.file.getPages().get(page).getGUID();
//		Gson gson = null;
//		Type type = new TypeToken<List<Music>>() {}.getType();
//		try {
//			ChordMessageInterface succ = dfs.getChord().locateSuccessor(pageGuid);
//			System.out.println(Long.toString(succ.getId()) + "/repository/" + Long.toString(pageGuid));
//			FileReader reader = new FileReader(Long.toString(succ.getId()) + "/repository/" + Long.toString(pageGuid));
//			music = gson.fromJson(reader, type);
//			reader.close();
//		}catch(FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (RemoteException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		System.out.println(music.size());
//		List<Song> songs = new ArrayList<>();
//
//		for(Music m : music){
//			if(m.getSong().getTitle().contains(text)){
//				songs.add(m.getSong());
//				break;
//			}
//			if(m.getArtist().getName().contains(text)){
//				songs.add(m.getSong());
//				break;
//			}
//			if(m.getArtist().getTerms().contains(text)){
//				songs.add(m.getSong());
//				break;
//			}
//		}
//
//		this.searchResult = gson.toJson(songs);
//		counter--;
//		System.out.println("Thread " + this.page + " finished running! counter at: " + this.counter);
//	}
	@Override
	public void run() {
		counter++;
		List<Music> music = null;
		Long pageGuid = this.file.getPages().get(page).getGUID();
		Gson gson = null;
		Type type = new TypeToken<List<Music>>() {}.getType();
		try {
			ChordMessageInterface succ = dfs.getChord().locateSuccessor(pageGuid);
			System.out.println(Long.toString(succ.getId()) + "/repository/" + Long.toString(pageGuid));
			FileReader reader = new FileReader(Long.toString(succ.getId()) + "/repository/" + Long.toString(pageGuid));
			music = gson.fromJson(reader, type);
			reader.close();
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(music.size());
		List<Song> songs = new ArrayList<>();

		for(Music m : music){
			if(m.getSong().getTitle().contains(text)){
				songs.add(m.getSong());
				break;
			}
			if(m.getArtist().getName().contains(text)){
				songs.add(m.getSong());
				break;
			}
			if(m.getArtist().getTerms().contains(text)){
				songs.add(m.getSong());
				break;
			}
		}

		this.searchResult = gson.toJson(songs);
		counter--;
		System.out.println("Thread " + this.page + " finished running! counter at: " + this.counter);
	}
}