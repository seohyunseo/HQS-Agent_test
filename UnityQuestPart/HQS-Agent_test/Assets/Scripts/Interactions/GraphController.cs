using System;
using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;
using UnityEngine.UI;

public class GraphController : MonoBehaviour
{
    public GameObject HRDot;
    public GameObject HRLine;
    public GameObject IMUDot;
    public GameObject IMULine;

    public Transform DotGroup;
    public Transform LineGroup;
    public RectTransform GraphArea;
    public TextMeshProUGUI HRText;
    public TextMeshProUGUI IMUText;

    private float graph_Width;
    private float graph_Height;                            
    private const int ArraySize = 30;                     
    private float[] HRValue = new float[ArraySize];
    private float[] IMUValue = new float[ArraySize];


    private GameObject[] HRDots = new GameObject[ArraySize];
    private GameObject[] HRLines = new GameObject[ArraySize];
    private GameObject[] IMUDots = new GameObject[ArraySize];
    private GameObject[] IMULines = new GameObject[ArraySize];

    private Vector2 prevHRDotPos = Vector2.zero;
    private Vector2 prevIMUDotPos = Vector2.zero;

    private int currentHRIndex = 0;
    private int currentIMUIndex = 0;


    private const int MaxHRValue = 200;
    private const int MaxIMUValue = 12;

    // Start is called before the first frame update
    void Start()
    {
        graph_Width = GraphArea.rect.width;
        graph_Height = GraphArea.rect.height;
    }

    public void UpdateHR(string hrData)
    {
        
        //Debug.Log("[InteractionManager] hr: " + hrData);
        float hr = float.Parse(hrData);
        HRValue[currentHRIndex] = hr;
        DrawHRGraph(HRValue[currentHRIndex], currentHRIndex);

        if (currentHRIndex + 1 == ArraySize)
        {
            for (int i = 0; i < HRLines.Length; i++)
            {
                if (HRLines[i] != null)
                    HRLines[i].SetActive(false);
            }
        }

        currentHRIndex = (currentHRIndex + 1) % ArraySize;

        HRText.text = "HR: " + hr.ToString("F2");
    }

    public void UpdateIMU(string imuData)
    {
        float imu = float.Parse(imuData);
        //Debug.Log("[InteractionManager] imu: " + imuData);
        IMUValue[currentIMUIndex] = imu;
        DrawIMUGraph(IMUValue[currentIMUIndex], currentIMUIndex);

        if (currentIMUIndex + 1 == ArraySize)
        {
            for (int i = 0; i < IMULines.Length; i++)
            {
                if (IMULines[i] != null) 
                    IMULines[i].SetActive(false);
            }
        }

        currentIMUIndex = (currentIMUIndex + 1) % ArraySize;

        IMUText.text = "Movement: " + imu.ToString("F2");
    }

    private void DrawHRGraph(float value, int index)
    {
        float startPosition = -graph_Width / 2;     
        float maxYPosition = graph_Height * 2 / 3;      

        if (HRDots[index] == null)
        {    
            HRDots[index] = Instantiate(HRDot, DotGroup, true);
        }

        
        RectTransform dotRT = HRDots[index].GetComponent<RectTransform>();
        Image dotImage = HRDots[index].GetComponent<Image>();

        float yPosOffset = value / (float)MaxHRValue; 

        dotRT.anchoredPosition = new Vector2(startPosition + (graph_Width / (ArraySize - 1) * index), maxYPosition * yPosOffset - graph_Height / 3);
        

        if (index != 0) 
        {
            if (HRLines[index] == null)
            {    
                HRLines[index] = Instantiate(HRLine, LineGroup, true);
            }

            RectTransform lineRT = HRLines[index].GetComponent<RectTransform>();
            Image lineImage = HRLines[index].GetComponent<Image>();

            float lineWidth = Vector2.Distance(prevHRDotPos, dotRT.anchoredPosition); 
            float xPos = (prevHRDotPos.x + dotRT.anchoredPosition.x) / 2;             
            float yPos = (prevHRDotPos.y + dotRT.anchoredPosition.y) / 2;             

            Vector2 dir = (dotRT.anchoredPosition - prevHRDotPos).normalized;
            float angle = Mathf.Atan2(dir.y, dir.x) * Mathf.Rad2Deg;
            lineRT.anchoredPosition = new Vector2(xPos, yPos);
            lineRT.SetSizeWithCurrentAnchors(RectTransform.Axis.Horizontal, lineWidth);
            lineRT.localRotation = Quaternion.Euler(0f, 0f, angle);
            lineRT.gameObject.SetActive(true);

        }

        prevHRDotPos = dotRT.anchoredPosition;    
    }

    private void DrawIMUGraph(float value, int index)
    {
        float startPosition = -graph_Width / 2;
        float maxYPosition = graph_Height * 2 / 3;

        if (IMUDots[index] == null)
        {
            IMUDots[index] = Instantiate(IMUDot, DotGroup, true);
        }


        RectTransform dotRT = IMUDots[index].GetComponent<RectTransform>();
        Image dotImage = IMUDots[index].GetComponent<Image>();

        float yPosOffset = value / (float)MaxIMUValue;

        dotRT.anchoredPosition = new Vector2(startPosition + (graph_Width / (ArraySize - 1) * index), maxYPosition * yPosOffset - graph_Height / 3);


        if (index != 0)
        {
            if (IMULines[index] == null)
            {
                IMULines[index] = Instantiate(IMULine, LineGroup, true);
            }

            RectTransform lineRT = IMULines[index].GetComponent<RectTransform>();
            Image lineImage = IMULines[index].GetComponent<Image>();

            float lineWidth = Vector2.Distance(prevIMUDotPos, dotRT.anchoredPosition);
            float xPos = (prevIMUDotPos.x + dotRT.anchoredPosition.x) / 2;
            float yPos = (prevIMUDotPos.y + dotRT.anchoredPosition.y) / 2;

            Vector2 dir = (dotRT.anchoredPosition - prevIMUDotPos).normalized;
            float angle = Mathf.Atan2(dir.y, dir.x) * Mathf.Rad2Deg;
            lineRT.anchoredPosition = new Vector2(xPos, yPos);
            lineRT.SetSizeWithCurrentAnchors(RectTransform.Axis.Horizontal, lineWidth);
            lineRT.localRotation = Quaternion.Euler(0f, 0f, angle);
            lineRT.gameObject.SetActive(true);

        }

        prevIMUDotPos = dotRT.anchoredPosition;
    }
}

