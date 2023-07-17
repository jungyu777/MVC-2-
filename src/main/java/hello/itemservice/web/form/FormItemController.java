package hello.itemservice.web.form;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/form/items")
@RequiredArgsConstructor
public class FormItemController {

    private final ItemRepository itemRepository;

    /**
     *
     * 스프링에서 제공하는 특별한 기능중하나 이 밑에 있는 컨트롤러를  사용하면
     * 자동으로 Model에 담기게된다
     * 그래서 밑에있는 코드들의 중복을 다 지울수 있다
     */
    @ModelAttribute("regions")
    public Map<String, String> regions(){
        Map<String,String> regions =new LinkedHashMap<>();
        regions.put("SEOUL","서울");
        regions.put("BUSAN","부산");
        regions.put("JEJU","제주");
        return regions;
    }
    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "form/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);


        return "form/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item",new Item());
        /**
         * 등록폼에서 멀티 체크박스가 나와야한다  그럴라면 컨트롤러에서 값을 넘겨주어야한다
         * 수정,등록, 아이템상에도 이 값이 필요하다 .하지만 이 코드의 중복을 한방에 해결해주는방법이있다
         * 스프링에서 특별한 기능을 하나 제공해준다
         * LinkedHashMap 을 쓰는 이유는 순서가 보장된다
         * 그냥 해쉬맵을 사용하게 되면 순서보장이 안된다
         * regions.put("SEOUL","서울"); SEOUL 은 KEY  서울 VALUE=사용자한테 보여주기위한 값
         */

        return "form/addForm";
    }

    @PostMapping("/add")
    public String addItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes) {
        log.info("item.open={}",item.getOpen());
        log.info("item.regions={}",item.getRegions());
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/form/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        return "form/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/form/items/{itemId}";
    }

}

