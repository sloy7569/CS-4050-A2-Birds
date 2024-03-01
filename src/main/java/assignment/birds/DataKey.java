package assignment.gotham_characters;

public class DataKey {
	private String characterName;
	private int characterSize;

	// default constructor
	public DataKey() {
		this(null, 0);
	}
        
	public DataKey(String name, int size) {
		characterName = name;
		characterSize = size;
	}

	public String getCharacterName() {
		return characterName;
	}

	public int getCharacterSize() {
		return characterSize;
	}

	/**
	 * Returns 0 if this DataKey is equal to k, returns -1 if this DataKey is smaller
	 * than k, and it returns 1 otherwise. 
	 */
	public int compareTo(DataKey k) {
            if (this.getCharacterSize() == k.getCharacterSize()) {
                int compare = this.characterName.compareTo(k.getCharacterName());
                if (compare == 0){
                     return 0;
                }
                else if (compare < 0) {
                    return -1;
                }
            }
            else if(this.getCharacterSize() < k.getCharacterSize()){
                    return -1;
            }
            return 1;

	}
}
