package com.example.biologyquizapplication.model;

import java.util.ArrayList;
import java.util.Random;
import java.util.ArrayList;
import java.util.Random;

public class QuestionDataAccess {
    private final ArrayList<Question> questions;
    private final ArrayList<Question> generator;

    public QuestionDataAccess() {
        questions = new ArrayList<>();
        populateList();

        System.out.println("GENERATOR REACHED");
        generator = new ArrayList<>(questions);
    }

    private void populateList() {
        questions.add(new Question("ما هو أكبر عضو في جسم الإنسان؟",
                new String[]{"الكبد", "القلب", "الرئتين"},
                3));
        questions.add(new Question("ما هو مصدر طاقة الخلية؟",
                new String[]{"النواة", "الميتوكوندريا", "جهاز جولجي",},
                1));
        questions.add(new Question("ما هو الغاز الأكثر وفرة في الغلاف الجوي للأرض؟",
                new String[]{"الأكسجين", "ثاني أكسيد الكربون", "النيتروجين"},
                2));
        questions.add(new Question("ما هي أصغر وحدة للحياة تسمى؟",
                new String[]{"الجزيء", "الخلية", "الذرة"},
                1));
        questions.add(new Question("ما هو العملية التي تقوم بها النباتات لصنع طعامها؟",
                new String[]{"التمثيل الضوئي", "التنفس", "الهضم"},
                0));
        questions.add(new Question("ما هي الوظيفة الرئيسية لكريات الدم البيضاء في جسم الإنسان؟"
                , new String[]{"نقل الأكسجين", "محاربة العدوى", "حمل العناصر الغذائية"},
                1));
        questions.add(new Question("ما هو اسم أكبر شريان في جسم الإنسان؟",
                new String[]{"الأبهر", "وريد كبير", "شريان رئوي"},
                0));
        questions.add(new Question("ما هو اسم الصبغة التي تعطي البشرة البشرة لونها؟",
                new String[]{"الميلانين", "الكيراتين", "الكولاجين"},
                0));
        questions.add(new Question("ما هو اسم الكيسات الصغيرة في الرئتين حيث يتم تبادل الأكسجين مقابل ثاني أكسيد الكربون؟",
                new String[]{"الشعب الهوائية", "الشعب الهوائية الصغيرة", "الحويصلات الهوائية"},
                2));
        questions.add(new Question("أي جزء من الدماغ البشري مسؤول عن التحكم في الحركة والتنسيق؟",
                new String[]{"المخيخ", "القشرة الدماغية", "ساق الدماغ"},
                0));
        questions.add(new Question("أي من الأنواع التالية ليس نوعًا من الأوعية الدموية؟",
                new String[]{"شريان", "وريد", "عضلة"},
                2));

    }

    public Question getRandomQuestion(){
        if (generator == null) return null;
        if (generator.isEmpty()){
            System.out.println("Generator is EMPTY");
            return null;
        }
        Question question = generator.get(new Random().nextInt(generator.size()));
        UserAnswersModule.usedQuestions.add(question);
        generator.remove(question);
        return question;
    }

}
