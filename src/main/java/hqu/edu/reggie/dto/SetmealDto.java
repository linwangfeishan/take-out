package hqu.edu.reggie.dto;



import hqu.edu.reggie.eneity.Setmeal;
import hqu.edu.reggie.eneity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
