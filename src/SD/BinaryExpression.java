package SD;

import java.util.Stack;


public class BinaryExpression {

    public static void main(String[] args) {
        // count the postfix
        System.out.println(countPostfix("23*54/+8-")); // -1
        System.out.println(countPostfix("2!3!^4+")); // 12
        System.out.println(countPostfix("23*54*+8-")); // 18

        // convert infix to postfix
        System.out.println("Postfix: " + infixToPostfix("2*3+5/4-8"));
        System.out.println("Postfix: " + infixToPostfix("2+3*4"));
            // convert then check if result is correct
        System.out.println("Postfix: " + infixToPostfix("((1+2)*(3^2+4))"));
        System.out.println(countPostfix("12+32^4+*")); // 39

        // convert infix to prefix
        System.out.println("Prefix: " + infixToPrefix("23*54/+8-"));
    }

    public static int countPostfix(String postfix_expression) {
        Stack<Integer> stack_of_operand = new Stack<>();
        boolean has_invalid_operator = false;

        for (char c : postfix_expression.toCharArray()) {

            // Valid operand
            if ("0123456789".indexOf(c) >= 0)
                stack_of_operand.push(Character.getNumericValue(c));
            // Valid operator
            else if ("+-*/^".indexOf(c) >= 0){
                // 2 then 1 because LIFO principle
                int operand_2 = stack_of_operand.pop();
                int operand_1 = stack_of_operand.pop();
                int result = 0;

                switch(c) {
                    case '+' -> result = operand_1 + operand_2;
                    case '-' -> result = operand_1 - operand_2;
                    case '*' -> result = operand_1 * operand_2;
                    case '/' -> result = operand_1 / operand_2;
                    case '^' -> result = (int) Math.pow(operand_1, operand_2);
//                    default -> throw new IllegalArgumentException("Operator " + c + " tidak ditemukan");
                }
                stack_of_operand.push(result);
            }
            else {
                if (!has_invalid_operator) {
                    System.out.print("Operator '" + c + "' tidak ditemukan. Hasil tanpa '" + c + "' adalah ");
                    has_invalid_operator = true;
                }
            }
        }
        return stack_of_operand.pop();
    }

    public static int operatorHierarchy(char operator) {
        switch(operator) {
            case '+': case '-': return 1;
            case '*': case'/': return 2;
            case '^': return 3;
            default: return -1;
        }
    }
    public static String infixToPostfix(String infix_expression) {
//        Queue<Integer> queue_of_operand = new Queue<>(); //FIFO
        Stack<Character> stack_of_operator = new Stack<>(); //LIFO
        StringBuilder postfix_expression = new StringBuilder();
        
        for (char c : infix_expression.toCharArray()) {

            if ("0123456789".indexOf(c) >= 0) {
                postfix_expression.append(c);
//                queue_of_operand.enqueue(Character.getNumericValue(c));
            } else if (c == '(') {
                stack_of_operator.push(c);
            } else if (c == ')') {
                // pop operator until find Left parentheses (
                while (!stack_of_operator.isEmpty() && stack_of_operator.peek() != '(') {
                    postfix_expression.append(stack_of_operator.pop());
                }
                // when ( is found:
                stack_of_operator.pop(); // pop Left parentheses (
            } else if ("+-*/^".indexOf(c) >= 0) {
                while (!stack_of_operator.isEmpty() && operatorHierarchy(stack_of_operator.peek()) >= operatorHierarchy(c)) {
                    postfix_expression.append(stack_of_operator.pop());
                }
                stack_of_operator.push(c);
            }
        }

//        while (!queue_of_operand.isEmpty()) {
//            postfix_expression.append(queue_of_operand.dequeue());
//        }

        while (!stack_of_operator.isEmpty()) {
            postfix_expression.append(stack_of_operator.pop());
        }

        return postfix_expression.toString();
    }

    public static String infixToPrefix(String infix_expression) {
        Stack<String> operandStack = new Stack<>();  // Stack for operands
        Stack<Character> operatorStack = new Stack<>();  // Stack for operators

        // Loop through each character in the infix expression
        for (int i = 0; i < infix_expression.length(); i++) {
            char c = infix_expression.charAt(i);

            // If it's a letter or digit, push it to the operand stack
            if (Character.isLetterOrDigit(c)) {
                operandStack.push(String.valueOf(c));
            }
            // If it's a left parenthesis, push it onto the operator stack
            else if (c == '(') {
                operatorStack.push(c);
            }
            // If it's a right parenthesis, pop until we find the matching (
            else if (c == ')') {
                while (!operatorStack.isEmpty() && operatorStack.peek() != '(') {
                    char operator = operatorStack.pop();
                    String operand2 = operandStack.pop();
                    String operand1 = operandStack.pop();
                    String expression = operator + operand1 + operand2;
                    operandStack.push(expression);
                }
                operatorStack.pop(); // Remove the (
            }
            // If it's an operator
            else if ("+-*/^".indexOf(c) >= 0) {
                // Pop operators with higher or equal precedence and apply them
                while (!operatorStack.isEmpty() && operatorHierarchy(operatorStack.peek()) >= operatorHierarchy(c)) {
                    char operator = operatorStack.pop();
                    String operand2 = operandStack.pop();
                    String operand1 = operandStack.pop();
                    String expression = operator + operand1 + operand2;
                    operandStack.push(expression);
                }
                operatorStack.push(c); // Push the current operator onto the stack
            }
        }

        // After the loop, pop all the remaining operators in the operator stack
        while (!operatorStack.isEmpty()) {
            char operator = operatorStack.pop();
            String operand2 = operandStack.pop();
            String operand1 = operandStack.pop();
            String expression = operator + operand1 + operand2;
            operandStack.push(expression);
        }

        // The result is the final expression in the operand stack
        return operandStack.pop();
    }

//    public static String infixToPrefix(String infix_expression) {
//        StringBuilder postfix_expression = new StringBuilder();
//
//        return postfix_expression.toString();
//    }
}
