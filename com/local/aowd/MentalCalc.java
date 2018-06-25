package com.local.aowd;

import java.io.*;
import java.util.HashMap;

public class MentalCalc {
    static HashMap<String, Skill> skillMap = new HashMap<String, Skill>();
    static HashMap<String, Skill> skillMapI = new HashMap<String, Skill>();
    static HashMap<String, Skill> skillMapJ = new HashMap<String, Skill>();
    static HashMap<String, Skill> skillMapS = new HashMap<String, Skill>();
    static HashMap<String, Mental> mentalMap = new HashMap<String, Mental>();
    static HashMap<String,HashMap<String, MentalConfig>> uniqueList = new HashMap<String,HashMap<String, MentalConfig>>();
    private static final int DEFAULT_THRESHOLD = 3;
    private static int threshold = 3;


    public static void main(String[] args) {
        //Load file with the skill/mental mapping
        MentalCalc mc = new MentalCalc();
        mc.loadSkillFile();
        mc.loadMentalFile();
        try {
            if (mc.calc()) {
                System.exit(0);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        System.exit(-1);
    }
    private boolean calc()
    {
        String removeMental=System.getProperty("REMOVE_SK");
        if(removeMental!=null && !"".equals(removeMental))
        {
            String[] removeList=removeMental.split("\\,");
            for(String sk:removeList)
            {
                removeSkill(sk);
            }
        }
        String sThreashold=System.getProperty("THRESHOLD");
        if(sThreashold!=null && !"".equals(sThreashold))
        {
            try
            {
                threshold=Integer.parseInt(sThreashold);
            }
            catch(Exception e)
            {
                threshold=DEFAULT_THRESHOLD;
            }
        }

        String specificMental=System.getProperty("MENTAL");

        String s1=null;
        String s2=null;
        String inner=null;
        String jianghu=null;
        String m1=null;
        String m2=null;
        String m3=null;

        s1=System.getProperty("SK_1");
        s2=System.getProperty("SK_2");
        inner=System.getProperty("S_I");
        jianghu=System.getProperty("J_I");
        m1=System.getProperty("SK_M1");
        m2=System.getProperty("SK_M2");
        m3=System.getProperty("SK_M3");

        level2(s1, s2, inner,jianghu, m1, m2, m3);
        for (String mental : uniqueList.keySet()) {
            if (specificMental!=null &&!mental.contains(specificMental))
            {
                continue;
            }
            System.out.println(uniqueList.get(mental).size()+ " "+mental);
            for(String m :uniqueList.get(mental).keySet())
            {
                if(uniqueList.get(mental).size()>=1)
                {
                    System.out.println(uniqueList.get(mental).get(m));
                    break;
                }
            }
        }
        return true;
    }

    public void removeSkill(String skill)
    {
        skillMap.remove(skill);
        skillMapJ.remove(skill);
    }


    public void level2(String s1, String s2, String i, String j, String m1, String m2, String m3) {
        MentalConfig mc = new MentalConfig();
        level1(mc, skillMap.get(s1), skillMap.get(s2), skillMap.get(i), skillMap.get(j), skillMap.get(m1), skillMap.get(m2), skillMap.get(m3));
    }

    public void level1(MentalConfig mc, Skill s, Skill s2, Skill i, Skill j, Skill m1, Skill m2, Skill m3) {
        mc = new MentalConfig();
        if (s != null) {
            mc.skill1 = s;
        } else {
            for (Skill sP : skillMapS.values()) {
                if (sP.equals(s2) || sP.equals(i) || sP.equals(j)  || sP.equals(m1) || sP.equals(m2) || sP.equals(m3)) {
                    continue;
                }
                level1(mc, sP, s2, i, j, m1, m2, m3);
            }
            return;
        }
        if (s2 != null) {
            mc.skill2 = s2;
        } else {
            for (Skill s2P : skillMapS.values()) {
                if (s2P.equals(s) || s2P.equals(i) || s2P.equals(j)  || s2P.equals(m1) || s2P.equals(m2) || s2P.equals(m3)) {
                    continue;
                }
                level1(mc, s, s2P, i, j, m1, m2, m3);
            }
            return;
        }
        if (i != null) {
            mc.inner = i;
        } else {
            for (Skill iP : skillMapI.values()) {
                if (iP.equals(s) || iP.equals(s2) || iP.equals(j)  || iP.equals(m1) || iP.equals(m2) || iP.equals(m3)) {
                    continue;
                }
                level1(mc, s, s2, iP, j, m1, m2, m3);
            }
            return;
        }
        if (j != null) {
            mc.jianghu = j;
        } else {
            for (Skill jP : skillMapJ.values()) {
                if (jP.equals(s) || jP.equals(s2) || jP.equals(i)  || jP.equals(m1) || jP.equals(m2) || jP.equals(m3)) {
                    continue;
                }
                level1(mc, s, s2, i, jP, m1, m2, m3);
            }
            return;
        }

        if (m1 != null) {
            mc.m1 = m1;
        } else {
            for (Skill m1P : skillMap.values()) {
                if (m1P.equals(s) || m1P.equals(s2) || m1P.equals(i) || m1P.equals(j) || m1P.equals(m2) || m1P.equals(m3)) {
                    continue;
                }
                level1(mc, s, s2, i, j, m1P, m2, m3);
            }
            return;
        }
        if (m2 != null) {
            mc.m2 = m2;
        } else {
            for (Skill m2P : skillMap.values()) {
                if (m2P.equals(s) || m2P.equals(s2) || m2P.equals(i) || m2P.equals(j) || m2P.equals(m1) || m2P.equals(m3)) {
                    continue;
                }
                level1(mc, s, s2, i, j, m1, m2P, m3);
            }
            return;
        }
        if (m3 != null) {
            mc.m3 = m3;
        } else {
            for (Skill m3P : skillMap.values()) {
                if (m3P.equals(s) || m3P.equals(s2) || m3P.equals(i) || m3P.equals(j) || m3P.equals(m1) || m3P.equals(m2)) {
                    continue;
                }
                level1(mc, s, s2, i, j, m1, m2, m3P);
            }
            return;
        }

        HashMap<String, MentalConfig> foundList = check(mc);
        String strList="";
        if (foundList.size() >= threshold) {
            for (String mental : foundList.keySet()) {
                strList+=" " +mental;
            }
            uniqueList.put(strList,foundList);
            /*
            System.out.println(foundList.size());
            for (String mental : foundList.keySet()) {
                System.out.println(mental + "," + foundList.get(mental));
            }
            */
        }
    }

    public HashMap<String, MentalConfig> check(MentalConfig c) {
        HashMap<String, MentalConfig> foundList = new HashMap<String, MentalConfig>();
        for (Mental m : mentalMap.values()) {
            if (foundList.containsKey(m.name)) {
                continue;
            }
            int countFound = 0;

            if (m.skillList.contains(c.skill1)) countFound++;
            if (m.skillList.contains(c.skill2)) countFound++;
            if (m.skillList.contains(c.inner)) countFound++;
            if (m.skillList.contains(c.jianghu)) countFound++;
            if (m.skillList.contains(c.m1)) countFound++;
            if (m.skillList.contains(c.m2)) countFound++;
            if (m.skillList.contains(c.m3)) countFound++;

            //Combination of 4 activates mental
            if (countFound >= 4) {
                foundList.put(m.name, c);
                //System.out.println(m.name+","+c);
            }
        }
        return foundList;
    }


    public void loadSkillFile() {
        String strLine = "";
        InputStream in = getClass().getClassLoader().getResourceAsStream("skill.csv");
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        try {
            strLine = br.readLine();
            while ((strLine = br.readLine()) != null) {
                if (strLine == null || "".equals(strLine)) {
                    continue;
                }
                String[] columns = strLine.split("\\,");
                if (columns != null && columns.length > 0) {
                    Skill s = new Skill();
                    s.name = columns[0];
                    switch (columns[1]) {
                        case "I":
                            s.type = SkillType.INTERNAL;
                            skillMapI.put(s.name, s);
                            break;
                        case "J":
                            s.type = SkillType.JIAGNHU;
                            skillMapJ.put(s.name, s);
                            break;
                        case "S":
                            s.type = SkillType.SKILL;
                            skillMapS.put(s.name, s);
                            break;
                        default:
                            break;
                    }
                    skillMap.put(columns[0], s);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMentalFile() {
        InputStream in = getClass().getClassLoader().getResourceAsStream("mental.csv");
        String strLine = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        try {
            strLine = br.readLine();
            while ((strLine = br.readLine()) != null) {
                if (strLine == null || "".equals(strLine)) {
                    continue;
                }
                String[] columns = strLine.split("\\,");
                if (columns != null && columns.length > 0) {
                    Skill s = skillMap.get(columns[1]);
                    if (s == null) {
                        s = new Skill();
                    }
                    Mental m = mentalMap.get(columns[0]);
                    if (m == null) {
                        m = new Mental();
                        m.name = columns[0];
                    }
                    m.skillList.add(s);
                    mentalMap.put(columns[0], m);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}