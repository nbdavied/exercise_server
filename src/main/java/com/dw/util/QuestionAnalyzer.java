package com.dw.util;

import com.dw.exercise.entity.Choice;
import com.dw.exercise.entity.Question;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.xmlbeans.XmlException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




public class QuestionAnalyzer {
	private String fileContent;
	private List<Question> questions;
	private int bankId;
	

	public QuestionAnalyzer(InputStream is){
		openFile(is);
		questions= new ArrayList<>();
		setBankId(0);
	}

	
	public void openFile(InputStream is){
		try {
			//OPCPackage opcPackage = POIXMLDocument.openPackage(path);
            XWPFDocument doc = new XWPFDocument(is);
			@SuppressWarnings("resource")
            XWPFWordExtractor  extractor = new XWPFWordExtractor(doc);
			fileContent = extractor.getText();

			//System.out.println(fileContent);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void startAnalyze() throws Exception {
		if(fileContent==null){
			throw new Exception("read file first");
		}
		String[] lines=fileContent.split("\n");
		int state=0;
		Question q = null;
		List<Choice> tmpChoices = new ArrayList<>();
		int target=0;
		for(int i=0;i<lines.length;i++){
			String line=trim(lines[i]);
			double process = (double)i/(double)lines.length*100;
			if(process>=target)
			{
				target+=5;
				System.out.println("扫描进度:"+(int)(process)+"%");
			}
			switch(state){
			case 0:
				//准备读入题型
				if(isUseless(line))
					break;
				if(typeChange(line)>0)
					state=typeChange(line);
				break;
			case 1:
				//准备读入判断题
				if(isUseless(line))
					break;
				if(typeChange(line)>0){
					state=typeChange(line);
					break;
				}
				if(endOfQuestion(line)){
					break;
				}
				if(isTFAnswer(line)){
					line = line.toLowerCase();
					List<Choice> rightChoices = new ArrayList<>();
					List<Choice> wrongChoices = new ArrayList<>();
					Choice right = new Choice();
					right.setRight(true);
					Choice wrong = new Choice();
					wrong.setRight(false);
					rightChoices.add(right);
					wrongChoices.add(wrong);
					q.setRightChoices(rightChoices);
					q.setWrongChoices(wrongChoices);
					if("t".equals(line)){
						right.setText("正确");
						wrong.setText("错误");
					}else if("f".equals(line)){
						right.setText("错误");
						wrong.setText("正确");
					}
					break;
				}
				q = readQuestion(line);
				q.setType("t");
				break;
			case 2:
				//准备读入单选题
				if(isUseless(line))
					break;
				if(typeChange(line)>0){
					state=typeChange(line);
					break;
				}
				if(endOfQuestion(line))
					break;
				if(isSingleAnswer(line))
				{
					int[] answerindex = parseAnswer(line);
                    parseAnswer(q, tmpChoices, answerindex);

					break;
				}
                tmpChoices = new ArrayList<>();
				q = readQuestion(line);
				q.setType("s");
				state = 4;
				break;
			case 3:
				//准备读入多选题
				if(isUseless(line))
					break;
				if(typeChange(line)>0){
					state=typeChange(line);
					break;
				}
				if(endOfQuestion(line))
					break;
				if(isMultiAnswer(line))
				{
					int[] answerindex = parseAnswer(line);
                    parseAnswer(q, tmpChoices, answerindex);

					break;
				}
                tmpChoices = new ArrayList<>();
				q = readQuestion(line);
				q.setType("m");
				state = 5;
				break;
			case 4:
				//准备读入单选题选项
				if(isUseless(line))
					break;
				if(q == null)
				{
					System.out.println("error state");
					state = 2;
					break;
				}
				if(tmpChoices.size() == 0 && typeChange(line) > 0){
					System.out.println("error: find no choice "+q.getQuestion());
					state=typeChange(line);
					questions.remove(q);
					break;
				}
				if(typeChange(line)>0)
				{
					System.out.println("error: answer not set: "+q.getQuestion());
					state=typeChange(line);
					questions.remove(q);
					break;
				}
				if(endOfQuestion(line)){
					state=2;
					break;
				}
				Choice c = new Choice();
				if(startWithABC(line)){
					c.setText(line.substring(2));
				}else{
					c.setText(line);
				}
				tmpChoices.add(c);
				break;
			case 5:
				//准备读入多选题选项
				if(isUseless(line))
					break;
				if(q == null)
				{
					System.out.println("error state");
					state = 3;
					break;
				}
				if(tmpChoices.size() ==0 && typeChange(line)>0){
					System.out.println("error: find no choice "+q.getQuestion());
					state=typeChange(line);
					questions.remove(q);
					break;
				}
				if(typeChange(line)>0)
				{
					System.out.println("error: answer not set: "+q.getQuestion());
					state=typeChange(line);
					questions.remove(q);
					break;
				}
				if(endOfQuestion(line))
				{
					state=3;
					break;
				}
				Choice m = new Choice();
				if(startWithABC(line)){
					m.setText(line.substring(2));
				}else{
					m.setText(line);
				}
				tmpChoices.add(m);
				break;
			}
			
		}
	}

    private void parseAnswer(Question q, List<Choice> tmpChoices, int[] answerindex) {
        for(int index = 0; index < tmpChoices.size(); index ++){
            if(contains(answerindex, index)){
                tmpChoices.get(index).setRight(true);
                q.getRightChoices().add(tmpChoices.get(index));
            }else{
                tmpChoices.get(index).setRight(false);
                q.getWrongChoices().add(tmpChoices.get(index));
            }
        }
    }

    //
	public List<Question> getQuestions(){
		return questions;
	}
	

	private Question readQuestion(String line){
		Question q = new Question();
		q.setQuestion(line);
		q.setBankId(bankId);
		q.setRightChoices(new ArrayList<>());
		q.setWrongChoices(new ArrayList<>());
		questions.add(q);
		return q;
	}
	private boolean isUseless(String line){
		if("".equals(line))
			return true;
		if(line==null)
			return true;
		
		if(line.startsWith("第"))
		{
			if(line.contains("章")||line.contains("节"))
				return true;
		}
		return false;
	}
	private boolean endOfQuestion(String line){
		if(line.startsWith("命题依据"))
			return true;
		if(line.startsWith("难度"))
			return true;
		if(line.startsWith("命题部室"))
			return true;
		return false;
	}
	private int typeChange(String line){
		if(line.contains("判断题"))
			return 1;
		if(line.contains("单选题"))
			return 2;
		if(line.contains("多选题"))
			return 3;
		return 0;
	}
	
	private boolean startWithABC(String line){
		String pattern="^[a-hA-H][.．].*";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(line);
		return m.matches();
	}
	
	private String trim(String s){
		String result = "";  
	    if(null!=s && !"".equals(s)){  
	        result = s.replaceAll("^[　*| *| *|\t|//s*]*", "").replaceAll("[　*| *| *|\t|//s*]*$", "")
	        		.replace("'", "\\'").replaceAll("\"", "\\\"");  
	    } 
	    
	    return result; 
	}
	private boolean isTFAnswer(String line) {
		String pattern = "^[tTfF]$";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(line);
		return m.matches();
	}
	private boolean isSingleAnswer(String line){
		String pattern = "^[\\s]*[abcdeABCDE][\\s]*$";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(line);
		return m.matches();
	}

    private boolean isMultiAnswer(String line){
		String pattern = "^[abcdeABCDE\\s]+$";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(line);
		return m.matches();
	}
	public int getBankId() {
		return bankId;
	}
	public void setBankId(int bankId) {
		this.bankId = bankId;
	}
    private int[] parseAnswer(String answerString){
        answerString = answerString.toLowerCase();
        char[] answers = answerString.toCharArray();
        int[] result = new int[answers.length];
        for (int i = 0; i < answers.length; i++) {
            char answer = answers[i];
            int r = answer - 'a';
            result[i] = r;
        }
        return result;
    }
    private boolean contains(int[] array, int i){
        for(int a : array){
            if(a == i){
                return true;
            }
        }
        return false;
    }
}
