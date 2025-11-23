package ca.bcit.comp2522.wordgame;

import java.util.random.RandomGenerator;

/**
 * Represents a question in the Word Game.
 * Contains a question prompt and the correct answer.
 *
 * @author David Martinez
 * @version 1.0
 */
public class Question
{
    private final String prompt;
    private final String answer;

    Question(final int questionType,
             final Country country)
    {
        validateQuestionType(questionType);

        final StringBuilder sb;
        sb = new StringBuilder();

        switch (questionType)
        {
            case WordGame.TYPE_CAPITAL_CITY:
                sb.append("'");
                sb.append(country.getCapitalCityName());
                sb.append("' is the capital of which country?");

                this.prompt = sb.toString();
                this.answer = country.getCountryName();

                break;
            case WordGame.TYPE_COUNTRY_NAME:
                sb.append("'");
                sb.append(country.getCountryName());
                sb.append("' , what is its capital city?");

                this.prompt = sb.toString();
                this.answer = country.getCapitalCityName();

                break;
            case WordGame.TYPE_FACT:
                final RandomGenerator rng;
                final int factIndex;
                final String fact;

                rng = RandomGenerator.getDefault();
                factIndex = rng.nextInt(WordGame.LOWER_BOUND, WordGame.UPPER_BOUND);
                fact = country.getFact(factIndex);

                sb.append("Fact: ");
                sb.append(fact);
                sb.append(" Which country does this describe?");

                this.prompt = sb.toString();
                this.answer = country.getCountryName();

                break;
            default:
                throw new IllegalArgumentException("Invalid question type");
        }
    }

    /*
    Validates that a string is neither null nor blank.
    Throws IllegalArgumentException if invalid.
     */
    private void validateQuestionType(final int i)
    {
        if (i < WordGame.LOWER_BOUND || i > WordGame.UPPER_BOUND)
        {
            throw new IllegalArgumentException("Invalid question type");
        }
    }

    /**
     * Gets the question prompt.
     *
     * @return the question prompt
     */
    public String getPrompt()
    {
        return prompt;
    }

    /**
     * Gets the correct answer.
     *
     * @return the correct answer
     */
    public String getAnswer()
    {
        return answer;
    }
}