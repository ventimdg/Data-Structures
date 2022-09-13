class Solution {

    public static int solution(String S) {
        int answer = 0;
        int curr = 1;
        for(int i = 0; i < S.length(); i++){
            if (curr == 1) {
                if (S.charAt(i) == 'a') {
                    answer += 0;
                } else if (S.charAt(i) == 'b') {
                    answer += 1;
                } else if (S.charAt(i) == 'c') {
                    answer += 2;
                }
                curr = 2;
            } else if (curr == 2){
                if (S.charAt(i) == 'a'){
                    answer += 2;
                }
                else if (S.charAt(i) == 'b'){
                    answer += 0;
                }
                else if (S.charAt(i) == 'c'){
                    answer += 1;
                }
                curr = 3;
            }
            else if (curr == 3) {
                if (S.charAt(i) == 'a'){
                    answer += 1;
                }
                else if (S.charAt(i) == 'b'){
                    answer += 2;
                }
                else if (S.charAt(i) == 'c'){
                    answer += 0;
                }
                curr = 1;
            }
        }
        return answer;
    }
}