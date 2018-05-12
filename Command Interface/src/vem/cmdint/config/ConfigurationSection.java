package vem.cmdint.config;

import java.util.ArrayList;
import java.util.List;

public class ConfigurationSection {

	private final ConfigurationSection parent;
	private List<ConfigurationSection> contents;
	private final String label;
	private String value;
	private List<String> data;

	public ConfigurationSection(String label, String value, ConfigurationSection parent) {
		this.label = label;
		this.value = value;
		this.parent = parent;
		if(parent!=null) parent.addSection(this);
		data = new ArrayList<String>();
		contents = new ArrayList<ConfigurationSection>();
	}

	public ConfigurationSection(String label, ConfigurationSection parent) {
		this(label, "", parent);
	}

	public ConfigurationSection createSubSection(String name){
		return createSubSection(name, "");
	}
	
	public ConfigurationSection createSubSection(String name, String val){
		return new ConfigurationSection(name, val, this);
	}
	
	//setStuffs
	public void set(String path, Object o){
		if(path==""){
			setValue(o);
			return;
		}
		
		ConfigurationSection cs = getConfigurationSection(path);
		if (cs != null) {
			cs.setValue(o);
		}else{
			String tmp = path;
			boolean hasDot;
			if(hasDot = tmp.contains(".")) tmp = tmp.substring(0, tmp.indexOf("."));
			ConfigurationSection test = getSubConfigSection(tmp);
			if(test==null)
				test = createSubSection(tmp);
			test.set(hasDot ? path.substring(path.indexOf(".")+1) : "", o);
			
		}
	}
	
	public void setValue(Object o){
		value = o.toString();
	}
	
	//Add stuffs
	public void addSection(ConfigurationSection cs) {
		try {
			if (getSubConfigSection(cs.getLabel()) != null)
				throw new Exception();

			contents.add(cs);
		} catch (Exception e) {
			System.err.println("Duplicate Configuration Section found! Not adding it to "+getAbsolutePath());
		}
	}
	
	public void addData(String s) {
		data.add(s);
	}

	//getStuffs
	public String getLabel() {
		return label;
	}

	public List<String> getData() {
		return data;
	}
	
	public ConfigurationSection getParentSection(){
		return parent;
	}
	
	public String getAbsolutePath(){
		String out = label;
		
		ConfigurationSection tmp = getParentSection();
		while(tmp!=null){
			out = tmp.getLabel() + "."+out;
			tmp = tmp.getParentSection();
		}
		
		return out;
	}

	public List<ConfigurationSection> getSubSections(){
		return contents;
	}
	
	private ConfigurationSection getSubConfigSection(String name) {
		for (ConfigurationSection cs : contents)
			if (cs.getLabel().equals(name))
				return cs;
		return null;
	}

	public ConfigurationSection getConfigurationSection(String path) {
		if (path.contains(".")) {
			ConfigurationSection sub = getSubConfigSection(path.substring(0, path.indexOf(".")));
			if (sub != null)
				return sub.getConfigurationSection(path.substring(path.indexOf(".") + 1));
		} else
			return getSubConfigSection(path);

		return null;
	}

	public void compile() {
		for (int i = 0; i < data.size();) {
			if (leadingZero(data.get(i)) == 0) {
				String nm = data.get(i).substring(0, data.get(i).indexOf(": "));
				String val = data.get(i).substring(data.get(i).indexOf(": ") + 2).trim();
				ConfigurationSection csi = new ConfigurationSection(nm, val, this);

				int first = -1;
				while (++i < data.size() && leadingZero(data.get(i)) > 0) {
					if (first < 0)
						first = leadingZero(data.get(i));
					csi.addData(data.get(i).substring(first));
				}
				csi.compile();
			}
		}
		data.clear();
	}

	public String toString(int sp) {
		String out = space(sp * 2) + label + ": " + value + "\n";
		for (ConfigurationSection cs : contents)
			out += cs.toString(sp + 1);

		return out;
	}

	private int leadingZero(String s) {
		int out = 0;
		for (;s.charAt(out) == ' ';out++);
		return out;
	}

	private String space(int i) {
		return String.format("%"+i+"s", "");
	}
}
