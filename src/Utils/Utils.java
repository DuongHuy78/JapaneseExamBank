package Utils;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.BaseFont;

import java.io.FileOutputStream;
import java.io.IOException;

import Dao.QuestionsDAO;
import Dao.SQLConnection;
import Models.*;


public class Utils {
	public static String scanImage(String urlImage) {
		try {
			String apiKey = "AIzaSyC2-5TvPDBuJzZHRsRNEshXYv_UXVTfvqc";
    
        	String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + 
            java.net.URLEncoder.encode(apiKey, "UTF-8");
        	//-preview-04-17
			URI uri = new URI(apiUrl);
			URL url = uri.toURL();
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setDoOutput(true);

			// Đọc file ảnh và encode dưới dạng Base64
            String imagePath = urlImage;
            byte[] imageBytes = Files.readAllBytes(Path.of(imagePath));
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            
            // JSON request với cả text và image
            String request = "{\n" +
                    "  \"contents\": [\n" +
                    "    {\n" +
                    "      \"parts\": [\n" +
                    "        {\n" +
                    "          \"text\": \"Read the questions from the image and return ONLY in the following format, no explanation.\\n"
								    + "If the section is Reading and there is a passage, include the passage in the \\\"Passage\\\" field for each question related to that passage.\\n"
								    + "Section: [Grammar/Vocabulary/Reading/Listening]\\n"
								    + "Passage: [Passage content, if any. If not, omit this field.]\\n"
								    + "Question: [Question content]\\n"
								    + "A. [Option A]\\n"
								    + "B. [Option B]\\n"
								    + "C. [Option C]\\n"
								    + "D. [Option D]\\n"
								    + "Answer: [A/B/C/D]\\n"
								    + "Next question\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"inline_data\": {\n" +
                    "            \"mime_type\": \"image/jpeg\",\n" +
                    "            \"data\": \"" + base64Image + "\"\n" +
                    "          }\n" +
                    "        }\n" +
                    "      ]\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";
                    


			try (OutputStream os = connection.getOutputStream()) {
				byte[] input = request.getBytes("utf-8");
				os.write(input, 0, input.length);
			}
			connection.getResponseCode();
	        InputStreamReader reader = new InputStreamReader(connection.getInputStream(), "utf-8");
	        try(BufferedReader br = new BufferedReader(reader)) {
	        	StringBuilder response = new StringBuilder();
	        	String line;
	        	while((line = br.readLine()) != null) {
	        		response.append(line.trim());
	        	}

				System.out.println("Response: " + response.toString());
	        	return response.toString();
	        }
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
        return null;
	}
	
	public static ArrayList<Questions> getQuestionsFromJson(String response) {
		ArrayList<Questions> questions = new ArrayList<>();
		String text = response;  // Sử dụng trực tiếp biến text đã nhận
		if (text == null || text.isEmpty()) {
			System.out.println("Văn bản trống");
			return questions;
		}
		
		System.out.println("Văn bản đầu vào: " + text.substring(0, Math.min(text.length(), 100)) + "...");
		
		// Chuẩn hóa các dấu xuống dòng
		text = text.replaceAll("\\r\\n|\\r", "\n");
		
		try {
			// Regex linh hoạt hơn
			Pattern questionPattern = Pattern.compile(
				"Section:\\s*(Grammar|Vocabulary|Reading|Listening)\\s*\n" +
				"(?:Passage:\\s*([\\s\\S]*?)\\s*\n)?" + 
				"Question:\\s*([\\s\\S]*?)\\s*\n" +
				"A\\.\\s*([\\s\\S]*?)\\s*\n" +
				"B\\.\\s*([\\s\\S]*?)\\s*\n" +
				"C\\.\\s*([\\s\\S]*?)\\s*\n" +
				"D\\.\\s*([\\s\\S]*?)\\s*\n" +
				"Answer:\\s*([A-D])",
				Pattern.UNICODE_CHARACTER_CLASS
			);
			
			Matcher matcher = questionPattern.matcher(text);
			int count = 0;
			
			while (matcher.find()) {
				count++;
				System.out.println("Tìm thấy câu hỏi thứ " + count);
				
				String section = matcher.group(1);
				String passage = matcher.group(2);
				String content = matcher.group(3);
				String optionA = matcher.group(4);
				String optionB = matcher.group(5);
				String optionC = matcher.group(6);
				String optionD = matcher.group(7);
				String correctAnswer = matcher.group(8);
				
				System.out.println("Section: " + section);
				System.out.println("Passage length: " + (passage != null ? passage.length() : 0));
				System.out.println("Question: " + content);
				
				// Nếu là Reading và có passage, nối passage vào content
				if ("Reading".equalsIgnoreCase(section) && passage != null && !passage.isEmpty()) {
					content = "Passage: " + passage + "\nQuestion: " + content;
				}
				
				List<Answers> answers = new ArrayList<>();
				answers.add(new Answers(0, 0, optionA, correctAnswer.equalsIgnoreCase("A")));
				answers.add(new Answers(0, 0, optionB, correctAnswer.equalsIgnoreCase("B")));
				answers.add(new Answers(0, 0, optionC, correctAnswer.equalsIgnoreCase("C")));
				answers.add(new Answers(0, 0, optionD, correctAnswer.equalsIgnoreCase("D")));
				
				Questions question = new Questions(0, content, section, null, null, answers);
				questions.add(question);
			}
			
			System.out.println("Tổng số câu hỏi tìm thấy: " + count);
			
			if (count == 0) {
				System.out.println("Không tìm thấy câu hỏi nào trong văn bản");
			}
		} catch (Exception e) {
			System.err.println("Lỗi xử lý regex: " + e.getMessage());
			e.printStackTrace();
		}
		
		return questions;
}
	
	public static String extractTextFromResponse(String jsonResponse) {
        Pattern textPattern = Pattern.compile("\"text\":\\s*\"(.+?)\"", Pattern.DOTALL);
        Matcher matcher = textPattern.matcher(jsonResponse);
        
       if (matcher.find()) {
       	
           String encodedText = matcher.group(1);
           // Xử lý các ký tự escape trong JSON
           return encodedText.replace("\\n", "\n")
                            .replace("\\r", "\r")
                            .replace("\\\"", "\"")
                            .replace("\\\\", "\\")
							.replace("\\u3000", " ");
       }
       return null;
    }
	
	public static int getNextQuestionOrder(int examId) {
		try {
			Connection conn = SQLConnection.getConnection();
			String sql = "SELECT MAX(questionOrder) FROM Exam_Questions WHERE ExamID = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, examId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) + 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1; // trả về 1 nếu ko có
	}

	public static int getNextQuestionId() {
		try {
			Connection conn = SQLConnection.getConnection();
			String sql = "SELECT MAX(ID) FROM Questions";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) + 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1; // trả về 1 nếu ko có
	}

	public static int getNextExamId() {
		try {
			Connection conn = SQLConnection.getConnection();
			String sql = "SELECT MAX(ID) FROM Exams";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) + 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1; // trả về 1 nếu ko có
	}
	
	public static ArrayList<Questions> getRamdomQuestions(String section, int number) {
		try {
			Connection conn = SQLConnection.getConnection();
			String sql = "SELECT * FROM Questions WHERE Section = ? ORDER BY RAND() LIMIT " + number;
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, section);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				ArrayList<Questions> questions = new ArrayList<>();
				do {
					Questions question = new Questions(0, null, null, null, null, null);
					question.setId(rs.getInt("ID"));
					question.setContent(rs.getString("Content"));
					question.setSection(rs.getString("Section"));
					question.setAudioPath(rs.getString("AudioPath"));
					question.setImagePath(rs.getString("ImagePath"));
					question.setAnswers(new ArrayList<Answers>());
					questions.add(question);
				} while (rs.next());
				return questions;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Exams getRandomExams(String nameExam, String level, int numGammar, int numVocab, int numReading, int numListening) {
		level = level.toUpperCase();
		if (!level.equals("N1") && !level.equals("N2") && !level.equals("N3") && !level.equals("N4") && !level.equals("N5")) {
			level = "N5";
		}
		System.out.println("Get next exam id: " + getNextExamId());
		Exams exam = new Exams(Utils.getNextExamId(), nameExam, level);
		ArrayList<Exam_Questions> examQuestions = new ArrayList<>();
		int questionOrder = getNextQuestionOrder(exam.getId());

		ArrayList<Questions> gammQuestions = new ArrayList<>();
		ArrayList<Questions> vocabQuestions = getRamdomQuestions("Vocabulary", numVocab);
		ArrayList<Questions> readingQuestions = getRamdomQuestions("Reading", numReading);
		ArrayList<Questions> listeningQuestions = getRamdomQuestions("Listening", numListening);
		gammQuestions = getRamdomQuestions("Grammar", numGammar);
		if (gammQuestions == null) {
			gammQuestions = new ArrayList<>();
		}
		vocabQuestions = getRamdomQuestions("Vocabulary", numVocab);
		if (vocabQuestions == null) {
			vocabQuestions = new ArrayList<>();
		}
		readingQuestions = getRamdomQuestions("Reading", numReading);
		if (readingQuestions == null) {
			readingQuestions = new ArrayList<>();
		}
		listeningQuestions = getRamdomQuestions("Listening", numListening);
		if (listeningQuestions == null) {
			listeningQuestions = new ArrayList<>();
		}
		ArrayList<Questions> allQuestions = new ArrayList<>();
		allQuestions.addAll(gammQuestions);
		allQuestions.addAll(vocabQuestions);
		allQuestions.addAll(readingQuestions);
		allQuestions.addAll(listeningQuestions);

		for (Questions question : allQuestions) {
			examQuestions.add(new Exam_Questions(exam.getId(), question.getId(), questionOrder));
			questionOrder++;
		}
		
		exam.setExamQuestions(examQuestions);
		return exam;
	}
	
	
	public static void exportToPDF(Exams exam) {
        Document document = new Document();

        try {
            // It's good practice to make the filename dynamic or configurable
            String fileName = exam.getTitle().replaceAll("[^a-zA-Z0-9]", "_") + "_" + exam.getLevel() + ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            String fontJapanese = "C:/Windows/Fonts/msgothic.ttc";
			String fontVietNamese = "C:/Windows/Fonts/Arial.ttf";
            BaseFont Japanese = BaseFont.createFont(fontJapanese + ",0", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			BaseFont VietNamese = BaseFont.createFont(fontVietNamese, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			// BaseFont baseFont = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

            Font titleFont = new Font(VietNamese, 18, Font.BOLD);
            Font headerFont = new Font(VietNamese, 16, Font.BOLD);
            Font questionFont = new Font(Japanese, 12);
			Font questionFontVN = new Font(VietNamese, 12);
            Font answerFont = new Font(Japanese, 12);
            Font correctAnswFont = new Font(Japanese, 12, Font.BOLDITALIC);

            // Add Exam Title and Level
            Paragraph examTitle = new Paragraph("Đề thi: " + exam.getTitle(), titleFont);
            examTitle.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(examTitle);

            Paragraph examLevel = new Paragraph("Trình độ: " + exam.getLevel(), headerFont);
            examLevel.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(examLevel);
            document.add(new Paragraph("\n"));

            if (exam.getExamQuestions() != null) {
				QuestionsDAO qDao = new QuestionsDAO();
                for (Exam_Questions eq : exam.getExamQuestions()) {
					Questions tempQuestion = new Questions(eq.getQuestionId(), null, null, null, null, null);
					Questions question = qDao.selectById(tempQuestion);
					document.add(new Paragraph("Câu " + eq.getQuestionOrder() + ":", headerFont));
					document.add(new Paragraph(question.getContent(), questionFont));

					//xử lý ảnh
					if (question.getImagePath() != null && !question.getImagePath().isEmpty()) {
                        try {
                            Image pdfImage = null;
							try {
								pdfImage = Image.getInstance(question.getImagePath());
							} catch (Exception fileEx) {
								System.err.println("Không thể tải hình ảnh từ đường dẫn: " + question.getImagePath() + " - " + fileEx.getMessage());
								document.add(new Paragraph("Lỗi tải hình ảnh: " + question.getImagePath(), questionFontVN));
							}
                            if (pdfImage != null) {
                                document.add(pdfImage);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            document.add(new Paragraph("Lỗi hiển thị hình ảnh.", questionFontVN));
                        }
                    }
					document.add(new Paragraph("A. " + question.getAnswers().get(0).getContent(), answerFont));
					document.add(new Paragraph("B. " + question.getAnswers().get(1).getContent(), answerFont));
					document.add(new Paragraph("C. " + question.getAnswers().get(2).getContent(), answerFont));
					document.add(new Paragraph("D. " + question.getAnswers().get(3).getContent(), answerFont));
                    document.add(new Paragraph("\n"));
                }

				for (Exam_Questions eq : exam.getExamQuestions()) {
					document.add(new Paragraph("Đáp án:", headerFont));
					document.add(new Paragraph("Câu " + eq.getQuestionOrder() + ":", headerFont));
					Questions tempQuestion = new Questions(eq.getQuestionId(), null, null, null, null, null);
					Questions question = qDao.selectById(tempQuestion);
					if(question.getAudioPath() != null) {
						document.add(new Paragraph("Mã câu hỏi: " + question.getId(), questionFontVN));
					}
					if (question.getAnswers() != null) {
						for (Answers answer : question.getAnswers()) {
							if (answer.isCorrect()) {
								document.add(new Paragraph("Đáp án đúng: " + answer.getContent(), correctAnswFont));
							}
						}
					} else {
						document.add(new Paragraph("Không có đáp án cho câu hỏi này.", questionFont));
					}
				}
            } else {
                document.add(new Paragraph("Đề thi này chưa có câu hỏi.", questionFont));
            }

            document.close();
            System.out.println("Tạo file PDF thành công: " + fileName);
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }
}
