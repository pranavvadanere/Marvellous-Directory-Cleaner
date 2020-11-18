import java.util.*;
import java.lang.*;
import java.io.*;
import java.io.FileInputStream;
import java.security.MessageDigest;

class Main{
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Please enter Directory name");
		String dir = br.readLine();
		Cleaner cobj = new Cleaner(dir);

	}
}

class Cleaner{
	public File fdir = null;

	public Cleaner(String name){
		fdir = new File(name);
		if(!fdir.exists()){
			System.out.println("Invalid Directory name");
			System.exit(0);
		}
	}
	public void cleanDirectoryEmptyFile(){
		File filelist[] = fdir.listFiles();
		int emptyFile = 0;
		for (File file : filelist){
			if(file.length() == 0){
				System.out.println("empty file name :"+file.getName());

				if(!file.delete()){
					System.out.println("unable to delete");

				}else{
					emptyFile++;
				}
			}

		}
		System.out.println("total empty files deleted "+emptyFile);
	}

	public void cleanDirectoryDuplicateFile() throws Exception{
		File filelist[] = fdir.listFiles();
		int dupFile = 0;

		byte bytearr[] = new byte[1024];

		LinkedList<String> lobj = new LinkedList<String>();

		int readCounter = 0;

		try{
			MessageDigest digest = MessageDigest.getInstance("MD5");
			if(digest == null){
				System.out.println("unable to get md5");
				System.exit(0);
			}
			for(File file : filelist){
				FileInputStream fis = new FileInputStream(file);

				if(file.length() != 0){
					while((readCounter = fis.read(bytearr)) != -1){
						digest.update(bytearr,0,readCounter);
					}
				}
				// hash byte of checksum
				byte bytes[] = digest.digest();

				StringBuilder sb = new StringBuilder();

				for(int i = 0; i < bytes.length; i++){
					sb.append(Integer.toString((bytes[i] & 0xff)+0x100,16).substring(1));
				}
				System.out.println("file name : " + file.getName() + "CheckSum : "+sb);

				if(lobj.contains(sb.toString())){
					if(!file.delete()){
						System.out.println("unable to delete file : "+ file.getName());
					}else{
						System.out.println("file get deleted : "+ file.getName());
						dupFile++;
					}
				}else{
					lobj.add(sb.toString());
				}
				fis.close();
			}
		}
		catch(Exception obj){
			System.out.println("Exception occured  "+ obj);
		}
		finally{

		}
		System.out.println("total duplicate file deleted "+ dupFile);
	}
}